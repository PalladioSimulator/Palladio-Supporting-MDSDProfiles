package edu.kit.ipd.sdq.mdsd.profiles.view.viewer;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class EObjectSorter {
	/**
	 * Reordering of the elements provided by content provider.
	 * @return
	 */
	protected ViewerSorter createGenericEObjectSorter() {
		return new ViewerSorter() {
			@Override
			public int category(Object element) {
				if (element instanceof EObject) {
					EObject eObject = (EObject) element;
					if (eObject.eContainmentFeature() != null) {
						eObject.eContainmentFeature().getFeatureID();
					}
				}
				return super.category(element);
			}

			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				if (e1 instanceof EObject && e2 instanceof EObject) {
					EObject eObject1 = (EObject) e1;
					EObject eObject2 = (EObject) e2;
					if (haveSameContainerAndContainmentFeature(eObject1,
							eObject2)) {
						int indexEObject1 = getIndex(eObject1);
						int indexEObject2 = getIndex(eObject2);
						if (indexEObject1 < indexEObject2) {
							return -1;
						} else if (indexEObject1 > indexEObject2) {
							return 1;
						} else {
							return 0;
						}
					}
				}
				return super.compare(viewer, e1, e2);
			}

			private boolean haveSameContainerAndContainmentFeature(
					EObject eObject1, EObject eObject2) {
				return eObject1.eContainer() != null
						&& eObject2.eContainer() != null
						&& eObject1.eContainer().equals(eObject2.eContainer())
						&& eObject1.eContainmentFeature().equals(
								eObject2.eContainmentFeature());
			}

			private int getIndex(EObject eObject) {
				if (eObject.eContainmentFeature().isMany()) {
					Object containmentValue = eObject.eContainer().eGet(
							eObject.eContainmentFeature());
					if (containmentValue instanceof List<?>) {
						List<?> list = (List<?>) containmentValue;
						return list.indexOf(eObject);
					}
				}
				return 0;
			}
		};
	}
}
