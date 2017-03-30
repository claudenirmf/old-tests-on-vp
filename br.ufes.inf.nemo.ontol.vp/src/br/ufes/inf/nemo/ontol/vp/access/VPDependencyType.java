package br.ufes.inf.nemo.ontol.vp.access;

import br.ufes.inf.nemo.ontol.model.CategorizationType;
import br.ufes.inf.nemo.ontol.vp.load.OntoLModelLoader;

public enum VPDependencyType {

	INSTANTIATION(OntoLModelLoader.STR_INSTANTIATION),
	SUBORDINATION(OntoLModelLoader.STR_SUBORDINATION),
	CATERGORIZATION(OntoLModelLoader.STR_CATEGORIZATION),
	COMPLETE_CATEGORIZATION(OntoLModelLoader.STR_COMPL_CATEGORZATION),
	DISJOINT_CATEGORIZATION(OntoLModelLoader.STR_DISJ_CATEGORIZATION),
	PARTITIONING(OntoLModelLoader.STR_PARTITIONS),
	POWERTYPING(OntoLModelLoader.STR_POWER_TYPE);
	
	private String stereotype_name;
	
	private VPDependencyType(String str_name) {
		this.stereotype_name = str_name;
	}
	
	public String str(){
		return this.stereotype_name;
	}
	
	public CategorizationType convert(){
		switch (this) {
		case CATERGORIZATION:
			return CategorizationType.CATEGORIZER;
		case COMPLETE_CATEGORIZATION:
			return CategorizationType.COMPLETE_CATEGORIZER;
		case DISJOINT_CATEGORIZATION:
			return CategorizationType.DISJOINT_CATEGORIZER;
		case PARTITIONING:
			return CategorizationType.PARTITIONER;
		default:
			return null;
		}
	}
	
	@Override
	public String toString() {
		return this.str();
	}

}
