package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.FormArmSchedule;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.Query;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

//
/**
 * The Class StudyParticipantAssignmentRepository.
 *
 * @author mehul
 */
@org.springframework.stereotype.Repository
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)

public class FormArmScheduleRepository implements Repository<FormArmSchedule, Query> {

    private GenericRepository genericRepository;

    public FormArmSchedule findById(Integer id) {
        return genericRepository.findById(FormArmSchedule.class, id);


    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public FormArmSchedule save(FormArmSchedule formArmSchedule) {
        return genericRepository.save(formArmSchedule);


    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(FormArmSchedule formArmSchedule) {
        throw new CtcAeSystemException("delete method not supported");

    }


    public Collection<FormArmSchedule> find(Query query) {
        return genericRepository.find(query);


    }

    public FormArmSchedule findSingle(Query query) {
        return genericRepository.findSingle(query);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}