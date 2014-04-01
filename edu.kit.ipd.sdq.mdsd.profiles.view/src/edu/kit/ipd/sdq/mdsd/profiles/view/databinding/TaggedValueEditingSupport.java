package edu.kit.ipd.sdq.mdsd.profiles.view.databinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.databinding.IEMFObservable;
import org.eclipse.emf.databinding.edit.IEMFEditObservable;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableItem;
import org.modelversioning.emfprofile.EMFProfilePackage;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofileapplication.StereotypeApplication;




//import edu.kit.ipd.sdq.mdsd.profiles.view.viewer.ChangedListener;
import edu.kit.ipd.sdq.mdsd.profiles.view.viewer.ProfilePropertiesView;

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

        if (((Object) attr.getEType()) instanceof String) {

            return textEditor;

        } else if (((Object) attr.getEType()) instanceof EEnum) {
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

            // return new ComboBoxCellEditor(viewer.getTable(), elements,
            // SWT.DROP_DOWN | SWT.READ_ONLY);
        } else if (((Object) attr.getEType()) instanceof EDataType) {
            logger.info(((Object) attr.getEType()));
            Boolean eDataType = (Boolean) ((EAttribute) attr).getDefaultValue();
//            Boolean eBoolean = Boolean.getBoolean(((EDataType) attr.getEType()).;
//            literals = eBoolean;
//            String[] elements = new String[literals.size()];
//            int i = 0;
//            for (EEnumLiteral literal : literals) {
//                elements[i++] = literal.getName();
//            }
            logger.info("asdfsadfasdfasdf " + eDataType);
            comboBoxEditor = new ComboBoxViewerCellEditor(viewer.getTable(), SWT.DROP_DOWN | SWT.READ_ONLY);
            comboBoxEditor.setLabelProvider(new LabelProvider());
            comboBoxEditor.setContenProvider(new ArrayContentProvider());      
//            comboBoxEditor.setInput(new Boolean[]{eDataType.booleanValue(), Boolean.valueOf(true)});
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
                if (cmd.canExecute()) {
                    editingDomain.getCommandStack().execute(cmd);
                }

            } else if (obj instanceof Integer) {
                cmd = SetCommand.create(editingDomain, ((IEMFEditObservable) element).getObserved(), attr,
                        Integer.valueOf((String) value));
                if (cmd.canExecute()) {
                    editingDomain.getCommandStack().execute(cmd);
                }

            } else if (obj instanceof EEnumLiteral) {
                EEnumLiteral newEnum = ((EEnumLiteral) obj).getEEnum().getEEnumLiteral((String) value);
                if (obj instanceof Enumerator) {
                    Enumerator oldEnum = (Enumerator) obj;
                    if (oldEnum != null) {
                        cmd = SetCommand.create(editingDomain, ((IEMFEditObservable) element).getObserved(), attr,
                                newEnum);
                        editingDomain.getCommandStack().execute(cmd);
                    }
                }

            } else if (obj instanceof Boolean) {
                // TODO: Implement Boolean combo box

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
