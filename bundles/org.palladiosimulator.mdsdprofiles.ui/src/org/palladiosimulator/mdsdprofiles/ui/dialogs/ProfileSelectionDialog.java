package org.palladiosimulator.mdsdprofiles.ui.dialogs;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.modelversioning.emfprofile.Profile;

/**
 * A dialog for selecting {@link Profile}s.
 * 
 * @author Max Schettler
 *
 */
public class ProfileSelectionDialog extends ElementListSelectionDialog {

	private static final String EMPTY_LIST_MESSAGE = "No Profiles can be selected";
	private static final String TITLE = "Select a Profile";
	private static final String EMPTY_SELECTION_MESSAGE = "You need to select a Profile to continue";

	public ProfileSelectionDialog(final Shell parent) {
		super(parent, new LabelProvider() {
			
			@Override
			public String getText(Object element) {
				return ((Profile) element).getName();
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
	 *             if not all elements are of the type {@link Profile}.
	 */
	@Override
	public void setElements(Object[] elements) {
		for (Object o : elements) {
			if (!(o instanceof Profile)) {
				throw new IllegalArgumentException("All elements must be of type \"Profile\"");
			}
		}
		super.setElements(elements);
	}

	/**
	 * Returns the selected {@link Profile}.
	 * 
	 * @return the profile
	 */
	public Profile getResultProfile() {
		return (Profile) getResult()[0];
	}

}
