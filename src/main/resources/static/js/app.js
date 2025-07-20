// Variables globales
let currentQuote = null
let currentFormData = null

// DOM Content Loaded
document.addEventListener("DOMContentLoaded", () => {
  console.log("DOM cargado, inicializando funciones...")
  initializeTabs()
  initializeQuoteForm()
  initializeTrackingForm()
  initializeVolumeCalculation()
  console.log("Funciones inicializadas correctamente")
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
  console.log("Inicializando formulario de cotización...")
  const form = document.getElementById("quote-form")
  const calculateBtn = document.getElementById("calculate-btn")
  const toggleBreakdownBtn = document.getElementById("toggle-breakdown")
  const acceptQuoteBtn = document.getElementById("accept-quote")

  if (!form) {
    console.error("No se encontró el formulario quote-form")
    return
  }
  
  if (!calculateBtn) {
    console.error("No se encontró el botón calculate-btn")
    return
  }

  console.log("Formulario y botón encontrados, agregando event listeners...")
  form.addEventListener("submit", handleQuoteSubmission)
  toggleBreakdownBtn.addEventListener("click", toggleCostBreakdown)
  acceptQuoteBtn.addEventListener("click", acceptQuote)
  console.log("Event listeners agregados correctamente")
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
    paymentMethod: formData.get("paymentMethod")
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
  console.log("Iniciando cálculo de cotización:", quoteData)
  const calculateBtn = document.getElementById("calculate-btn")
  const btnText = calculateBtn.querySelector(".btn-text")
  const loadingSpinner = calculateBtn.querySelector(".loading-spinner")

  // Show loading state
  btnText.style.display = "none"
  loadingSpinner.style.display = "flex"
  calculateBtn.disabled = true

  try {
    console.log("Enviando request a /api/quote/calculate")
    const response = await fetch("/api/quote/calculate", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(quoteData),
    })

    console.log("Response status:", response.status)
    
    if (response.ok) {
      const result = await response.json()
      console.log("Resultado recibido:", result)
      currentQuote = result
      displayQuoteResult(result)
    } else {
      const errorText = await response.text()
      console.error("Error response:", errorText)
      throw new Error("Error al calcular la cotización: " + response.status)
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

  // Mostrar modal de método de pago
  document.getElementById('payment-modal').style.display = 'flex';

  // Confirmar pago
  document.getElementById('confirm-payment-btn').onclick = async function() {
    const paymentMethod = document.getElementById('paymentMethodModal').value;
    document.getElementById('payment-modal').style.display = 'none';
    await processPayment(paymentMethod);
  };
  // Cancelar
  document.getElementById('cancel-payment-btn').onclick = function() {
    document.getElementById('payment-modal').style.display = 'none';
  };
}

async function processPayment(paymentMethod) {
  const acceptBtn = document.getElementById("accept-quote")
  acceptBtn.disabled = true
  acceptBtn.textContent = "Procesando..."

  try {
    // Obtener usuario logueado
    const user = JSON.parse(localStorage.getItem("user"))
    if (!user) {
      alert("Debe iniciar sesión para continuar")
      acceptBtn.disabled = false
      acceptBtn.textContent = "Aceptar y Reservar Viaje"
      return
    }

    const requestData = {
      ...currentFormData,
      totalCost: currentQuote.totalCost,
      clienteId: user.id,
      metodoPago: paymentMethod
    }

    const response = await fetch("/api/quote/accept", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(requestData),
    })

    if (response.ok) {
      const result = await response.json()
      
      if (result.success) {
        alert(`¡Cotización aceptada! Se le asignará un transportista en breve.\nCódigo de reserva: ${result.codigoSeguimiento}`)
        // Reset form
        document.getElementById("quote-form").reset()
        document.getElementById("quote-result").style.display = "none"
        document.getElementById("volume-display").style.display = "none"
        currentQuote = null
        currentFormData = null
        loadMisEnvios();
      } else {
        alert(`Error: ${result.message}`)
      }
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
}

// Función placeholder para cargar envíos del cliente
function loadMisEnvios() {
  console.log("Función loadMisEnvios llamada")
  // Esta función se implementará cuando sea necesario
} 