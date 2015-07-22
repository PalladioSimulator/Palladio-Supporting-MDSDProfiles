package org.palladiosimulator.mdsdprofiles.ui.dialogs;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.modelversioning.emfprofile.Stereotype;

/**
 * A dialog for selecting {@link Stereotype}s.
 * 
 * @author Max Schettler
 *
 */
public class StereotypeSelectionDialog extends ElementListSelectionDialog {

	private static final String EMPTY_LIST_MESSAGE = "No Stereotypes can be selected";
	private static final String TITLE = "Select a Stereotype";
	private static final String EMPTY_SELECTION_MESSAGE = "You need to select a Stereotype to continue";

	public StereotypeSelectionDialog(final Shell parent) {
		super(parent, new LabelProvider() {
			
			@Override
			public String getText(Object element) {
				return ((Stereotype) element).getName();
			}

			@Override
			public Image getImage(Object element) {
				return null;
			}
		});
		setValidator(new ISelectionStatusValidator() {

			@Override
			public IStatus validate(Object[] selection) {
				return selection.length == 1 ? Status.OK_STATUS : Status.CANCEL_STATUS;
			}
		});

		setTitle(TITLE);
		setEmptySelectionMessage(EMPTY_SELECTION_MESSAGE);
		setEmptyListMessage(EMPTY_LIST_MESSAGE);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws IllegalArgumentException
	 *             if not all elements are of the type {@link Stereotype}.
	 */
	@Override
	public void setElements(Object[] elements) {
		for (Object o : elements) {
			if (!(o instanceof Stereotype)) {
				throw new IllegalArgumentException("All elements must be of type \"Stereotype\"");
			}
		}
		super.setElements(elements);
	}

	/**
	 * Returns the selected {@link Stereotype}.
	 * 
	 * @return the stereotype
	 */
	public Stereotype getResultStereotype() {
		return (Stereotype) getResult()[0];
	}

}
