package edu.kit.ipd.sdq.mdsd.profiles.view.databinding;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.databinding.edit.IEMFEditObservable;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
//import edu.kit.ipd.sdq.mdsd.profiles.view.viewer.ChangedListener;

public class TaggedValueEditingSupport extends EditingSupport {

    private static Logger logger = Logger.getLogger(TaggedValueEditingSupport.class);

    Map<String, CellEditor> celleditors = new HashMap<String, CellEditor>();
    private TableViewer viewer;
    private EditingDomain editingDomain;

    private CellEditor textEditor;
    private ComboBoxViewerCellEditor comboBoxEditor;

    // ArrayList<String> items = new ArrayList<>();
    // String[] elements = new String[] {};

    private List<EEnumLiteral> literals;
    private EStructuralFeature feature;

    public TaggedValueEditingSupport(TableViewer tableViewer, EditingDomain editingDomain) {
        super(tableViewer);
        this.viewer = tableViewer;
        this.editingDomain = editingDomain;
        textEditor = new TextCellEditor(viewer.getTable());
    }

    public TaggedValueEditingSupport(TableViewer tableViewer, EditingDomain editingDomain,
            EStructuralFeature eStructuralFeature) {
        super(tableViewer);
        this.viewer = tableViewer;
        this.editingDomain = editingDomain;
        this.feature = eStructuralFeature;
        textEditor = new TextCellEditor(viewer.getTable());
    }

    @Override
    protected boolean canEdit(Object element) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected CellEditor getCellEditor(Object element) {
        EAttribute attr = (EAttribute) ((IEMFEditObservable) element).getStructuralFeature();
        Object obj = ((EObject) ((IEMFEditObservable) element).getObserved()).eGet(attr);
        Object dataType = (Object) attr.getEType();
        
        if (dataType instanceof EEnum) {
            logger.info(((Object) attr.getEType()));

            EEnum eEnum = (EEnum) ((EAttribute) attr).getEType();
            literals = eEnum.getELiterals();
            String[] elements = new String[literals.size()];
            int i = 0;
            for (EEnumLiteral literal : literals) {
                elements[i++] = literal.getName();
            }

            comboBoxEditor = new ComboBoxViewerCellEditor(viewer.getTable(), SWT.DROP_DOWN | SWT.READ_ONLY);
            comboBoxEditor.setLabelProvider(new LabelProvider());
            comboBoxEditor.setContenProvider(new ArrayContentProvider());
            int j = 0;
            do {
                comboBoxEditor.setInput(elements);
                j++;
            } while (j <= 0);
            return comboBoxEditor;

        } else if (dataType instanceof EDataType) {
        	EDataType eDataType = (EDataType) dataType;
        	
        	if (eDataType.getInstanceClass() == String.class || eDataType.getInstanceClass() == int.class) {
        		return textEditor;
        	
        	} else if (eDataType.getInstanceClass() == Boolean.class || eDataType.getInstanceClass() == Boolean.TYPE) {
        		comboBoxEditor = new ComboBoxViewerCellEditor(viewer.getTable(), SWT.DROP_DOWN | SWT.READ_ONLY);
        		comboBoxEditor.setLabelProvider(new LabelProvider());
        		comboBoxEditor.setContenProvider(new ArrayContentProvider());      
        		comboBoxEditor.setInput(Arrays.asList(new Boolean[] {(Boolean) obj, !(Boolean) obj}));
        		
        	}
            return comboBoxEditor;
            
        } else {
            return new TextCellEditor(viewer.getTable());

        }
    }

    @Override
    protected Object getValue(Object element) {
        logger.info(element);
        EAttribute attr = (EAttribute) ((IEMFEditObservable) element).getStructuralFeature();
        Object obj = ((EObject) ((IEMFEditObservable) element).getObserved()).eGet(attr);
        logger.info(attr);
        logger.info(obj);
        logger.info(obj.getClass());
        return obj.toString();
    }

    @Override
    protected void setValue(Object element, Object value) {
        try {
            EAttribute attr = (EAttribute) ((IEMFEditObservable) element).getStructuralFeature();
            Object obj = ((EObject) ((IEMFEditObservable) element).getObserved()).eGet(attr);
            editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(attr.eContainer());
            Command cmd = null;

            if (obj instanceof String) {
                cmd = SetCommand.create(editingDomain, ((IEMFEditObservable) element).getObserved(), attr, value);
                if (!obj.equals(value) && cmd.canExecute()) {
                    editingDomain.getCommandStack().execute(cmd);
                }

            } else if (obj instanceof Integer) {
                cmd = SetCommand.create(editingDomain, ((IEMFEditObservable) element).getObserved(), attr,
                        Integer.valueOf((String) value));
                if (!obj.toString().equals(value) && cmd.canExecute()) {
                    editingDomain.getCommandStack().execute(cmd);
                }

            } else if (obj instanceof EEnumLiteral) {
                EEnumLiteral newEnum = ((EEnumLiteral) obj).getEEnum().getEEnumLiteral((String) value);
                if (obj instanceof Enumerator) {
                    Enumerator oldEnum = (Enumerator) obj;
                    if (oldEnum != null && oldEnum != newEnum) {
                        cmd = SetCommand.create(editingDomain, ((IEMFEditObservable) element).getObserved(), attr,
                                newEnum);
                        editingDomain.getCommandStack().execute(cmd);
                    }
                }

            } else if (obj instanceof Boolean) {
            	Boolean oldBoolean = Boolean.valueOf((Boolean) obj);
            	Boolean newBoolean = Boolean.valueOf((Boolean) value);
        			if (newBoolean == null || (newBoolean.toString()).trim().length() == 0) {
                        cmd = SetCommand.create(editingDomain, ((IEMFEditObservable) element).getObserved(), attr, 
                        		null);
                        editingDomain.getCommandStack().execute(cmd);
        			} else if (oldBoolean != null && oldBoolean != newBoolean) {
                        cmd = SetCommand.create(editingDomain, ((IEMFEditObservable) element).getObserved(), attr, 
                        		newBoolean);
                        editingDomain.getCommandStack().execute(cmd);
        			}
        			
            } else {
                // do nothing
            }
            viewer.update(element, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A common method for all enums since they can't have another base class
     * 
     * @param <T>
     *            Enum type
     * @param c
     *            enum type. All enums must be all caps.
     * @param string
     *            case insensitive
     * @return corresponding enum, or null
     */
    public static <T extends Enum<T>> T getEnumFromString(Class<T> c, String string) {
        if (c != null && string != null) {
            try {
                return Enum.valueOf(c, string.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
