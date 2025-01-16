package co.quest.xms.valuation.api;

import co.quest.xms.valuation.api.controller.MagicFormulaController;
import co.quest.xms.valuation.api.dto.MagicFormulaResult;
import co.quest.xms.valuation.application.service.MagicFormulaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MagicFormulaController.class)
class MagicFormulaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MagicFormulaService magicFormulaService;

    @Test
    void calculateMagicFormula_shouldReturnMagicFormulaResult() throws Exception {
        // Arrange
        String symbol = "AAPL";
        MagicFormulaResult result = new MagicFormulaResult(symbol, 0.2, 0.1);
        when(magicFormulaService.calculateMagicFormula(symbol)).thenReturn(result);

        // Act & Assert
        mockMvc.perform(get("/api/v1/magic-formula/{symbol}", symbol))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol").value(symbol))
                .andExpect(jsonPath("$.returnOnCapital").value(0.2))
                .andExpect(jsonPath("$.earningsYield").value(0.1));
    }
}
