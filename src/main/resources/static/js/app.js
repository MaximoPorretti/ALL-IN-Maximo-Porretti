// Variables globales
let currentQuote = null
let currentFormData = null

// DOM Content Loaded
// Cuando el DOM está listo, inicializa las funciones principales
document.addEventListener("DOMContentLoaded", () => {
  initializeTabs()
  initializeQuoteForm()
  initializeTrackingForm()
  initializeVolumeCalculation()
})

// Funcionalidad de pestañas
function initializeTabs() {
  const tabButtons = document.querySelectorAll(".tab-button")
  const tabContents = document.querySelectorAll(".tab-content")

  tabButtons.forEach((button) => {
    button.addEventListener("click", () => {
      const tabId = button.getAttribute("data-tab")

      // Remove active class from all buttons and contents
      tabButtons.forEach((btn) => btn.classList.remove("active"))
      tabContents.forEach((content) => content.classList.remove("active"))

      // Add active class to clicked button and corresponding content
      button.classList.add("active")
      document.getElementById(tabId + "-tab").classList.add("active")
    })
  })
}

// Funcionalidad del formulario de cotización
function initializeQuoteForm() {
  const form = document.getElementById("quote-form")
  const calculateBtn = document.getElementById("calculate-btn")
  const toggleBreakdownBtn = document.getElementById("toggle-breakdown")
  const acceptQuoteBtn = document.getElementById("accept-quote")

  form.addEventListener("submit", handleQuoteSubmission)
  toggleBreakdownBtn.addEventListener("click", toggleCostBreakdown)
  acceptQuoteBtn.addEventListener("click", acceptQuote)
}

// Cálculo de volumen
function initializeVolumeCalculation() {
  const heightInput = document.getElementById("height")
  const widthInput = document.getElementById("width")
  const lengthInput = document.getElementById("length")
  const volumeDisplay = document.getElementById("volume-display")
  const volumeValue = document.getElementById("volume-value")

  function calculateAndDisplayVolume() {
    const height = Number.parseFloat(heightInput.value) || 0
    const width = Number.parseFloat(widthInput.value) || 0
    const length = Number.parseFloat(lengthInput.value) || 0

    if (height > 0 && width > 0 && length > 0) {
      const volume = (height * width * length) / 1000000 // cm³ to m³
      volumeValue.textContent = volume.toFixed(3)
      volumeDisplay.style.display = "block"
    } else {
      volumeDisplay.style.display = "none"
    }
  }

  heightInput.addEventListener("input", calculateAndDisplayVolume)
  widthInput.addEventListener("input", calculateAndDisplayVolume)
  lengthInput.addEventListener("input", calculateAndDisplayVolume)
}

// Manejar el envío del formulario de cotización
async function handleQuoteSubmission(event) {
  event.preventDefault()

  const formData = new FormData(event.target)
  const quoteData = {
    origin: formData.get("origin"),
    destination: formData.get("destination"),
    weight: Number.parseFloat(formData.get("weight")),
    height: Number.parseFloat(formData.get("height")) || 0,
    width: Number.parseFloat(formData.get("width")) || 0,
    length: Number.parseFloat(formData.get("length")) || 0,
    urgency: formData.get("urgency") === "on",
    scheduledDate: formData.get("scheduledDate"),
  }

  // Validate required fields
  if (!quoteData.origin || !quoteData.destination || !quoteData.weight) {
    alert("Por favor complete todos los campos obligatorios")
    return
  }

  currentFormData = quoteData
  await calculateQuote(quoteData)
}

// Calcular cotización
async function calculateQuote(quoteData) {
  const calculateBtn = document.getElementById("calculate-btn")
  const btnText = calculateBtn.querySelector(".btn-text")
  const loadingSpinner = calculateBtn.querySelector(".loading-spinner")

  // Show loading state
  btnText.style.display = "none"
  loadingSpinner.style.display = "flex"
  calculateBtn.disabled = true

  try {
    const response = await fetch("/api/quote/calculate", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(quoteData),
    })

    if (response.ok) {
      const result = await response.json()
      currentQuote = result
      displayQuoteResult(result)
    } else {
      throw new Error("Error al calcular la cotización")
    }
  } catch (error) {
    console.error("Error:", error)
    alert("Error al calcular la cotización. Por favor intente nuevamente.")
  } finally {
    // Hide loading state
    btnText.style.display = "block"
    loadingSpinner.style.display = "none"
    calculateBtn.disabled = false
  }
}

// Mostrar resultado de la cotización
function displayQuoteResult(result) {
  const quoteResultDiv = document.getElementById("quote-result")
  const totalPrice = document.getElementById("total-price")
  const validUntil = document.getElementById("valid-until")
  const estimatedDays = document.getElementById("estimated-days")
  const distance = document.getElementById("distance")
  const driversBadge = document.getElementById("drivers-badge")

  // Update main quote information
  totalPrice.textContent = `$${result.totalCost.toLocaleString("es-AR")}`
  validUntil.textContent = result.validUntil
  estimatedDays.textContent = result.estimatedDays
  distance.textContent = Math.round(result.baseDistance)
  driversBadge.textContent = `${result.availableDrivers} transportistas disponibles`

  // Update cost breakdown
  updateCostBreakdown(result)

  // Show the result card with animation
  quoteResultDiv.style.display = "block"
  quoteResultDiv.classList.add("animate-fadeIn")
}

// Actualizar desglose de costos
function updateCostBreakdown(result) {
  document.getElementById("distance-cost").textContent = `$${result.distanceCost.toLocaleString("es-AR")}`
  document.getElementById("weight-cost").textContent = `$${result.weightCost.toLocaleString("es-AR")}`
  document.getElementById("breakdown-total").textContent = `$${result.totalCost.toLocaleString("es-AR")}`

  // Volume cost (show only if > 0)
  const volumeCostItem = document.getElementById("volume-cost-item")
  if (result.volumeCost > 0) {
    document.getElementById("volume-cost").textContent = `$${result.volumeCost.toLocaleString("es-AR")}`
    volumeCostItem.style.display = "flex"
  } else {
    volumeCostItem.style.display = "none"
  }

  // Urgency cost (show only if > 0)
  const urgencyCostItem = document.getElementById("urgency-cost-item")
  if (result.urgencyCost > 0) {
    document.getElementById("urgency-cost").textContent = `$${result.urgencyCost.toLocaleString("es-AR")}`
    urgencyCostItem.style.display = "flex"
  } else {
    urgencyCostItem.style.display = "none"
  }
}

// Alternar visibilidad del desglose de costos
function toggleCostBreakdown() {
  const breakdown = document.getElementById("cost-breakdown")
  const toggleBtn = document.getElementById("toggle-breakdown")

  if (breakdown.style.display === "none" || breakdown.style.display === "") {
    breakdown.style.display = "block"
    toggleBtn.textContent = "Ocultar Desglose de Costos"
  } else {
    breakdown.style.display = "none"
    toggleBtn.textContent = "Ver Desglose de Costos"
  }
}

// Aceptar cotización
async function acceptQuote() {
  if (!currentFormData || !currentQuote) {
    alert("Error: No hay cotización disponible")
    return
  }

  const acceptBtn = document.getElementById("accept-quote")
  acceptBtn.disabled = true
  acceptBtn.textContent = "Procesando..."

  try {
    const response = await fetch("/api/quote/accept", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(currentFormData),
    })

    if (response.ok) {
      const trackingCode = await response.text()
      alert(`¡Cotización aceptada! Se le asignará un transportista en breve.\nCódigo de reserva: ${trackingCode}`)

      // Reset form
      document.getElementById("quote-form").reset()
      document.getElementById("quote-result").style.display = "none"
      document.getElementById("volume-display").style.display = "none"
      currentQuote = null
      currentFormData = null
    } else {
      throw new Error("Error al procesar la solicitud")
    }
  } catch (error) {
    console.error("Error:", error)
    alert("Error al procesar la solicitud. Por favor intente nuevamente.")
  } finally {
    acceptBtn.disabled = false
    acceptBtn.textContent = "Aceptar y Reservar Viaje"
  }
}

// Tracking form functionality
function initializeTrackingForm() {
  const form = document.getElementById("tracking-form")
  form.addEventListener("submit", handleTrackingSubmission)
}

// Handle tracking form submission
async function handleTrackingSubmission(event) {
  event.preventDefault()

  const trackingCodeInput = document.getElementById("tracking-code")
  const trackingCode = trackingCodeInput.value.trim()
  const resultDiv = document.getElementById("tracking-result")

  resultDiv.style.display = "none"
  resultDiv.textContent = ""

  if (!trackingCode) {
    alert("Por favor ingrese el código de reserva")
    return
  }

  try {
    const response = await fetch(`/api/tracking/${encodeURIComponent(trackingCode)}`)

    if (response.ok) {
      const data = await response.json()
      resultDiv.innerHTML = `
        <p><strong>Origen:</strong> ${data.origin}</p>
        <p><strong>Destino:</strong> ${data.destination}</p>
        <p><strong>Estado:</strong> ${data.status}</p>
      `
    } else if (response.status === 404) {
      resultDiv.textContent = "Envío no encontrado"
    } else {
      throw new Error("Error al obtener el seguimiento")
    }
  } catch (error) {
    console.error("Error:", error)
    resultDiv.textContent = "Ocurrió un error al consultar el seguimiento"
  } finally {
    resultDiv.style.display = "block"
  }
}
