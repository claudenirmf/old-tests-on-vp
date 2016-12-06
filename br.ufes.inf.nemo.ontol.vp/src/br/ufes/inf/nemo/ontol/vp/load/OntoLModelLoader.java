package br.ufes.inf.nemo.ontol.vp.load;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.model.IClass;
import com.vp.plugin.model.IDependency;
import com.vp.plugin.model.IModelElement;
import com.vp.plugin.model.IPackage;
import com.vp.plugin.model.IStereotype;
import com.vp.plugin.model.ITaggedValue;
import com.vp.plugin.model.ITaggedValueContainer;
import com.vp.plugin.model.ITaggedValueDefinition;
import com.vp.plugin.model.ITaggedValueDefinitionContainer;
import com.vp.plugin.model.factory.IModelElementFactory;

import br.ufes.inf.nemo.ontol.model.CategorizationType;
import br.ufes.inf.nemo.ontol.model.EntityDeclaration;
import br.ufes.inf.nemo.ontol.model.FOClass;
import br.ufes.inf.nemo.ontol.model.HOClass;
import br.ufes.inf.nemo.ontol.model.Individual;
import br.ufes.inf.nemo.ontol.model.Model;
import br.ufes.inf.nemo.ontol.model.ModelElement;
import br.ufes.inf.nemo.ontol.model.ModelPackage;
import br.ufes.inf.nemo.ontol.model.OntoLClass;
import br.ufes.inf.nemo.ontol.model.OrderlessClass;
import br.ufes.inf.nemo.ontol.vp.access.VPClass;
import br.ufes.inf.nemo.ontol.vp.access.VPDependency;
import br.ufes.inf.nemo.ontol.vp.access.VPDependencyType;
import br.ufes.inf.nemo.ontol.vp.access.VPModelAccess;
import br.ufes.inf.nemo.ontol.vp.access.VPModelElement;
import br.ufes.inf.nemo.ontol.vp.access.VPPackage;

public class OntoLModelLoader {

	public static final String STR_INDIVIDUAL = "individual";
	public static final String STR_FIRST_ORDER_CLASS = "firstorder";
	public static final String STR_HIGH_ORDER_CLASS = "highorder";
	public static final String STR_ORDERLESS_CLASS = "orderless";
	public static final String TG_VALLUE_ORDER = "order";
	
	public static final String STR_INSTANTIATION =  "instantiation";
	public static final String STR_SUBORDINATION =  "subordination";
	public static final String STR_POWER_TYPE =  "powertype";
	public static final String STR_CATEGORIZATION =  "categorization";
	public static final String STR_COMPL_CATEGORZATION =  "completecategorization";
	public static final String STR_DISJ_CATEGORIZATION =  "disjointcategorization";
	public static final String STR_PARTITIONS =  "partitions";
	
	public static final String BASE_TYPE_CLASS = IModelElementFactory.MODEL_TYPE_CLASS;
	public static final String BASE_TYPE_DEPENDENCY = IModelElementFactory.MODEL_TYPE_DEPENDENCY;
	
	private static final String DEFAULT_ORDER = "2";
	
	private static ResourceSet rs;
	private static Set<IStereotype> stereotypes;
	
	public static synchronized List<Resource> getResources(){
		return getResourceSet().getResources();
	}
	
	public static synchronized boolean isResourceSetLoaded(){
		return rs!=null;
	}
	
	public static synchronized void clear(){
		if(getResourceSet()!=null)
			getResourceSet().getResources().clear();
		if(stereotypes!=null)
			for (IStereotype str : stereotypes)
				str.delete();
	}
	
	public static synchronized void loadModel(String name, String path) {
		// Initialize the model
		@SuppressWarnings("unused")
		ModelPackage mp = ModelPackage.eINSTANCE;
		
		// Register the XMI resource factory for the .xmi extension
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> map = reg.getExtensionToFactoryMap();
		map.put("*", new XMIResourceFactoryImpl());
		
		// Obtain a new resource set
		ResourceSet rs = getResourceSet();	clear();
		
		// Get the resource
		Resource importedModel = rs.createResource(URI.createURI(path+"\\"+name));
		
        try {
			FileInputStream file = new FileInputStream(path+"\\"+name);
			importedModel.load(file, null);
			file.close();
			
			for (EObject o : importedModel.getContents()) {
				if(o instanceof Model){
					Model m = (Model) o;
					System.out.println("Model "+m.getName()+" was loaded.");
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found in '"+path+"\\"+name+"'.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void feed() {
		for (ModelElement element : getLoadedModelElements())
			feed(element);
	}
	
	public static void update() {
		for (ModelElement elem : getLoadedModelElements()) {
			if(elem instanceof OntoLClass){
				OntoLClass c = (OntoLClass) elem;
				VPClass vpc = (VPClass) VPModelAccess.getModelElement(getFullyQualifiedName(c));
				setStereotype(c,vpc);
				updateSpecializations(c,vpc);
				updateInstantiations(c,vpc);
				updateSubordinations(c,vpc);
				updateCategorizations(c,vpc);
				updatePowertyping(c,vpc);
				updateReferences(c,vpc);
				updateAttributes(c,vpc);
				if(elem instanceof HOClass)
					vpc.setOrder((HOClass) elem);
			} else if(elem instanceof Individual) {
				Individual i = (Individual) elem;
				VPClass vpc = (VPClass) VPModelAccess.getModelElement(getFullyQualifiedName(i));
				setStereotype(i,vpc);
				updateInstantiations(i,vpc);
			}
		}
	}

	public static String getFullyQualifiedName(ModelElement element) {
		if(element instanceof OntoLClass){
			return ((Model) element.eContainer()).getName() 
					+ '.' + ((OntoLClass) element).getName();
		} else {
			return "";
		}
	}

	public static IStereotype getStereotypeByNameAndBaseType(String strName, String baseType) {
		// Loads strList
		if (stereotypes == null || stereotypes.size() == 0) {
			stereotypes = new HashSet<IStereotype>();
			for (IModelElement e : ApplicationManager.instance().getProjectManager().getProject()
					.toAllLevelModelElementArray(IModelElementFactory.MODEL_TYPE_STEREOTYPE))
				stereotypes.add((IStereotype) e);
		}
		// Returns the stereotype in case it is found
		for(IModelElement m : stereotypes){
			final IStereotype s = (IStereotype) m;
			if(s.getName()==strName && s.getBaseType()==baseType)
				return s;
		}
		// If the stereotype does not exist, its is created, recorded and returned
		final IStereotype s = IModelElementFactory.instance().createStereotype();
		s.setName(strName);
		s.setBaseType(baseType);
		if(strName==STR_HIGH_ORDER_CLASS){
			ITaggedValueDefinitionContainer container = IModelElementFactory.instance().createTaggedValueDefinitionContainer();
			s.setTaggedValueDefinitions(container);
			ITaggedValueDefinition order = container.createTaggedValueDefinition();
			order.setName(TG_VALLUE_ORDER);
			order.setType(ITaggedValueDefinition.TYPE_INTEGER);
			order.setDefaultValue(DEFAULT_ORDER);
		}
		stereotypes.add(s);
		return s;
	}

	private static ResourceSet getResourceSet(){
		return (rs==null) ? rs=new ResourceSetImpl() : rs ;
	}

	private static Set<Model> getLoadedModels(){
		Set<Model> models = new HashSet<Model>();
		for (Resource resource : getResources())
			for (EObject eo : resource.getContents())
				if(eo instanceof Model)
					models.add((Model) eo);
		return models;
	}

	private static Set<ModelElement> getLoadedModelElements(){
		Set<ModelElement> elements = new HashSet<ModelElement>();
		for (Model m : getLoadedModels())
			elements.addAll(m.getElements());
		return elements;
	}

	private static IStereotype getStereotypeOf(EntityDeclaration element) {
		if(element instanceof FOClass){
			return getStereotypeByNameAndBaseType(STR_FIRST_ORDER_CLASS, BASE_TYPE_CLASS);
		} else if(element instanceof HOClass){
			return getStereotypeByNameAndBaseType(STR_HIGH_ORDER_CLASS, BASE_TYPE_CLASS);
		} else if(element instanceof OrderlessClass){
			return getStereotypeByNameAndBaseType(STR_ORDERLESS_CLASS, BASE_TYPE_CLASS);
		} else {
			return getStereotypeByNameAndBaseType(STR_INDIVIDUAL, BASE_TYPE_CLASS);
		}
	}

	private static void feed(ModelElement element) {
		if(element instanceof EntityDeclaration){
			String fqn = getFullyQualifiedName(element);
			VPModelElement vpc = VPModelAccess.getModelElement(fqn);
			if(vpc == null) {
				VPModelElement vpp = VPModelAccess.getModelElement(((Model) element.eContainer()).getName());
				if(vpp == null)	vpp = create((Model) element.eContainer());
				vpc = create((EntityDeclaration) element,(VPPackage) vpp);
			}
		}
	}
	
	private static VPPackage create(Model model) {
		IPackage pkg = IModelElementFactory.instance().createPackage();
		pkg.setName(model.getName());
		VPPackage vpp = (VPPackage) VPModelElement.wrap(pkg);
		VPModelAccess.register(vpp);
		return vpp;
	}

	private static VPClass create(EntityDeclaration element, VPPackage vpp) {
		IClass c = IModelElementFactory.instance().createClass();
		c.setName(element.getName());
		vpp.getVPSource().addChild(c);
		VPClass vpc = (VPClass) VPModelElement.wrap(c);
		VPModelAccess.register(vpc);
		return vpc;
	}

	private static void updateSpecializations(OntoLClass c, VPClass vpc) {
		Set<VPClass> oldSupers = new HashSet<VPClass>();
		Set<OntoLClass> ontoLSupers = new HashSet<OntoLClass>();
		oldSupers.addAll(vpc.getSuperClasses());
		ontoLSupers.addAll(c.getSuperClasses());
		// Remove old supers
		for (VPClass vpClass : oldSupers) {
			String vpc_fqn = vpc.getFullyQualifiedName();
			boolean found = false;
			for (OntoLClass ontoLSuper : ontoLSupers)
				if(!ontoLSuper.eIsProxy() && vpc_fqn == getFullyQualifiedName(ontoLSuper)) {
					found = true;
					ontoLSupers.remove(ontoLSuper);
					break;
				}
			if(!found)
				try { vpc.removeSuper(vpClass); } 
				catch (Exception e) { e.printStackTrace(); }
		}
		// Add new supers
		for (OntoLClass ontoLSuper : ontoLSupers) {
			if(ontoLSuper.eIsProxy())	continue;
			// If it is not a proxy, verifies if it already exists
			String ol_fqn = getFullyQualifiedName(ontoLSuper);
			boolean found = false;
			for (VPClass vpClass : oldSupers)
				if(ol_fqn == vpClass.getFullyQualifiedName()){
					found = true;
					break;
				}
			if(!found){
				vpc.addSuper(ontoLSuper);
			}
		}
	}

	private static void updateInstantiations(EntityDeclaration e, VPClass vpc) {
		Set<VPClass> firsts = vpc.getInstantiatedClasses();
		List<OntoLClass> seconds = e.getInstantiatedClasses();
		// Who is on first but not on second must leave
		for (VPClass first : firsts) {
			String fqn1 = first.getFullyQualifiedName();
			boolean found = false;
			for (OntoLClass second : seconds) {
				String fqn2 = getFullyQualifiedName(second);
				if(fqn1 == fqn2){
					found = true;
					break;
				}
			}
			if(!found){
				firsts.remove(first);
				// TODO delete old instantiation dependency relation
			}
		}
		// Who is on second but not on first must stay
		for (OntoLClass second : seconds){
			String fqn2 = getFullyQualifiedName(second);
			boolean found = false;
			for (VPClass first : firsts)
				if(fqn2 == first.getFullyQualifiedName()){
					found = true;
					break;
				}
			if(!found){
				vpc.addInstantiatedClass(second);
			}
		}
	}

	private static void updateSubordinations(OntoLClass c, VPClass vpc) {
		// TODO Auto-generated method stub
		Set<VPClass> firsts = vpc.getSubordinatorClasses();
		List<OntoLClass> seconds = c.getSubordinators();
		// Who is on first but not on second must leave
		for (VPClass first : firsts) {
			String fqn1 = first.getFullyQualifiedName();
			boolean found = false;
			for (OntoLClass second : seconds) {
				String fqn2 = getFullyQualifiedName(second);
				if(fqn1 == fqn2){
					found = true;
					break;
				}
			}
			if(!found){
				firsts.remove(first);
				// TODO delete old subordination dependency relation
			}
		}
		// Who is on second but not on first must stay
		for (OntoLClass second : seconds){
			String fqn2 = getFullyQualifiedName(second);
			boolean found = false;
			for (VPClass first : firsts)
				if(fqn2 == first.getFullyQualifiedName()){
					found = true;
					break;
				}
			if(!found){
				vpc.addSubordinatorClass(second);
			}
		}
	}

	private static void updateCategorizations(OntoLClass c, VPClass vpc) {
		VPDependency vpd = vpc.getDependencyToBasetype();
		OntoLClass basetype = c.getBasetype();
		OntoLClass basepwt = c.getPowertypeOf();
		// No current basetype
		if(vpd == null){
			if(basetype!=null)
				vpc.addBasetypeClass(basetype,c.getCategorizationType());
			else if(basepwt!=null)
				vpc.addBasetypeClass(basepwt);
			return	;
		}
		// Check for a match
		VPClass basevpc = vpd.getTarget();
		String fqn = basevpc.getFullyQualifiedName();
		VPDependencyType vpdtype = vpd.getType();
		if(fqn==getFullyQualifiedName(basepwt) && vpdtype ==VPDependencyType.POWERTYPING){
			return	;
		} else if(fqn==getFullyQualifiedName(basetype) && vpdtype.convert()==basetype.getCategorizationType()) {
			return	;
		}
		// In case of no match, delete and recreate
		vpd.getVPSource().delete();
		if(basetype!=null)
			vpc.addBasetypeClass(basetype,c.getCategorizationType());
		else if(basepwt!=null)
			vpc.addBasetypeClass(basepwt);
		return	;
	}

	private static void updatePowertyping(OntoLClass c, VPClass vpc) {
		// TODO break updateCategorizations into two methods
	}

	private static void updateReferences(OntoLClass c, VPClass vpc) {
		// TODO Auto-generated method stub
		
	}

	private static void updateAttributes(OntoLClass c, VPClass vpc) {
		// TODO Auto-generated method stub
		
	}

	private static void setStereotype(EntityDeclaration e, VPClass vpc) {
		IClass source = vpc.getVPSource();
		// Clear previous stereotypes
		IStereotype[] old = source.toStereotypeModelArray();
		if (old != null)
			for (IStereotype str : old)
				source.removeStereotype(str);
		// Add proper stereotype
		source.addStereotype(getStereotypeOf(e));
	}
	
}
