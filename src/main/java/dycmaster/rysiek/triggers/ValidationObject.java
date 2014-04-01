package dycmaster.rysiek.triggers;

import dycmaster.rysiek.shared.Create;
import dycmaster.rysiek.triggers.triggerParsers.CompoundTriggerLogicalCriterion;

import java.util.Collection;


public class ValidationObject {
	Collection<Trigger> triggers = Create.newCollection();

	public ValidationObject(CompoundTriggerLogicalCriterion criterionToValidate){
		this.criterionToValidate = criterionToValidate;
	}

	public CompoundTriggerLogicalCriterion getCriterionToValidate() {
		return criterionToValidate;
	}

	public void setCriterionToValidate(CompoundTriggerLogicalCriterion criterionToValidate) {
		this.criterionToValidate = criterionToValidate;
	}

	public Collection<Trigger> getTriggers() {
		return triggers;
	}

	public void setTriggers(Collection<Trigger> triggers) {
		this.triggers = triggers;
	}

	CompoundTriggerLogicalCriterion criterionToValidate;


}
