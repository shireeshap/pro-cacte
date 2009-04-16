package gov.nih.nci.ctcae.web.rules;

import gov.nih.nci.ctcae.web.rules.AbstractRulesTest;
import gov.nih.nci.ctcae.core.domain.*;

import java.util.List;
import java.util.ArrayList;

import com.semanticbits.rules.brxml.*;
import com.semanticbits.rules.objectgraph.FactResolver;
import org.apache.commons.lang.StringUtils;
import junit.framework.TestCase;


public class FactResolverTest extends TestCase {

    public void testFactResolver_1() throws Exception {
        StudyParticipantCrfItem studyParticipantCrfItem = new StudyParticipantCrfItem();

        ProCtcValidValue proCtcValidValue = new ProCtcValidValue();
        proCtcValidValue.setValue("High");

        studyParticipantCrfItem.setProCtcValidValue(proCtcValidValue);
        studyParticipantCrfItem.setCrfPageItem(new CrfPageItem());
        studyParticipantCrfItem.getCrfPageItem().setProCtcQuestion(new ProCtcQuestion());
        studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion().setProCtcQuestionType(ProCtcQuestionType.SEVERITY);
        FactResolver factResolver = new FactResolver();

        boolean result = factResolver.assertFact(studyParticipantCrfItem, "gov.nih.nci.ctcae.core.domain.ProCtcValidValue","value", "High","==");
        assertTrue(result);

//        proCtcValidValue.setValue("Severe");
////        studyParticipantCrfItem.setProCtcValidValue(proCtcValidValue);
//        result = factResolver.assertFact(proCtcValidValue, null,
//                "value", "Mild", "==");
//        assertFalse(result);

    }


}