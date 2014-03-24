package edu.kit.ipd.sdq.mdsd.profiles.view.editing;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;
import org.modelversioning.emfprofile.Stereotype;

import edu.kit.ipd.sdq.mdsd.profiles.view.viewer.ProfilePropertiesView;

public class TaggedValueCellModifier implements ICellModifier{
	
	public TaggedValueCellModifier(ProfilePropertiesView profilePropertiesView) {
		super();
	}

	@Override
	public boolean canModify(Object element, String property) {
		return true;
	}

	@Override
	public Object getValue(Object element, String property) {

	    Object result = null;
	    Stereotype node = (Stereotype) element;
	    
	    result = node.getName();
	    
		return result;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		
		TableItem tableItem = (TableItem) element;
		Stereotype node = (Stereotype) tableItem.getData();
		String valueString;
		
		valueString = ((String) value).trim();
		node.setName(valueString);
	}

}
