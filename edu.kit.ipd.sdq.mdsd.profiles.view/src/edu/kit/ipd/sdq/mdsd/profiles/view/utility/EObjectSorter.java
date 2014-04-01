package edu.kit.ipd.sdq.mdsd.profiles.view.utility;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class EObjectSorter {
    /**
     * Reordering of the elements provided by content provider.
     * 
     * @return
     */
    public ViewerSorter createGenericEObjectSorter() {
        return new ViewerSorter() {
            @Override
            public int category(final Object element) {
                if (element instanceof EObject) {
                    final EObject eObject = (EObject) element;
                    if (eObject.eContainmentFeature() != null) {
                        eObject.eContainmentFeature().getFeatureID();
                    }
                }
                return super.category(element);
            }

            @Override
            public int compare(final Viewer viewer, final Object e1, final Object e2) {
                if (e1 instanceof EObject && e2 instanceof EObject) {
                    final EObject eObject1 = (EObject) e1;
                    final EObject eObject2 = (EObject) e2;
                    if (this.haveSameContainerAndContainmentFeature(eObject1, eObject2)) {
                        final int indexEObject1 = this.getIndex(eObject1);
                        final int indexEObject2 = this.getIndex(eObject2);
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

            private boolean haveSameContainerAndContainmentFeature(final EObject eObject1, final EObject eObject2) {
                return eObject1.eContainer() != null && eObject2.eContainer() != null
                        && eObject1.eContainer().equals(eObject2.eContainer())
                        && eObject1.eContainmentFeature().equals(eObject2.eContainmentFeature());
            }

            private int getIndex(final EObject eObject) {
                if (eObject.eContainmentFeature().isMany()) {
                    final Object containmentValue = eObject.eContainer().eGet(eObject.eContainmentFeature());
                    if (containmentValue instanceof List<?>) {
                        final List<?> list = (List<?>) containmentValue;
                        return list.indexOf(eObject);
                    }
                }
                return 0;
            }
        };
    }
}
