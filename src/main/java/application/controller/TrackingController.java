package application.controller;

import application.service.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tracking")
public class TrackingController {

    @Autowired
    private TrackingService trackingService;

    @GetMapping("/{code}")
    public ResponseEntity<TrackingService.TrackingInfo> getTracking(@PathVariable String code) {
        TrackingService.TrackingInfo info = trackingService.getTracking(code);
        if (info == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(info);
    }
}
