package lv.krists.templateconfigurator;

import lv.krists.templateconfigurator.odtfilehandling.helper.OdtDomHelper;
import org.junit.jupiter.api.Test;

import static org.springframework.test.util.AssertionErrors.assertTrue;

public class OdtDomHelperTest {
    @Test
    void testClearImportMask() {
        OdtDomHelper reader = new OdtDomHelper();
        String input = "[import block1.dt]";
        String expected = "block1.dt";
        assertTrue("Import mask should equal 'my_block'", reader.clearImportMask(input).equals(expected));
    }

    @Test
    void testNonImportString() {
        OdtDomHelper reader = new OdtDomHelper();
        String input = "doesnt_contain_mask";
        assertTrue("Import mask has nothing to change", reader.clearImportMask(input).equals(input));
    }
}
