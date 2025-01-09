package co.quest.xms.valuation.api.controller;

import co.quest.xms.valuation.api.dto.MagicFormulaResult;
import co.quest.xms.valuation.application.service.MagicFormulaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/magic-formula")
@RequiredArgsConstructor
public class MagicFormulaController {

    private final MagicFormulaService magicFormulaService;

    @GetMapping("/{symbol}")
    public ResponseEntity<MagicFormulaResult> calculateMagicFormula(@PathVariable String symbol) {
        MagicFormulaResult result = magicFormulaService.calculateMagicFormula(symbol);
        return ResponseEntity.ok(result);
    }
}
