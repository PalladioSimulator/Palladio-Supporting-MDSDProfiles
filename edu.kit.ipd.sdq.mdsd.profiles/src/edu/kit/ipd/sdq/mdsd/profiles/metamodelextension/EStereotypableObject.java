package edu.kit.ipd.sdq.mdsd.profiles.metamodelextension;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.modelversioning.emfprofile.Profile;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofileapplication.ProfileApplication;
import org.modelversioning.emfprofileapplication.StereotypeApplication;

/**
 * A representation of the model object '<em><b>EStereotypableObject</b></em>'.
 * 
 * It is intended to be the root of all objects to which a stereotype can be
 * applied.
 * 
 * @author Matthias Eisenmann
 * 
 */
public interface EStereotypableObject extends EObject {

    /**
     * Retrieves the stereotype applications for this element.
     * 
     * @return The stereotype applications for this element.
     */
    EList<StereotypeApplication> getStereotypeApplications();
    
    /**
     * Retrieves all applications of stereotypes of the specified profile for this element.
     * 
     * @param profile
     *            The profile for which to retrieve all stereotype applications.
     * @return The applications of stereotypes of the specified profile for this element, or an
     *         empty list if no such stereotype application exists.
     */
	EList<StereotypeApplication> getStereotypeApplications(Profile profile);

    /**
     * Retrieves the applications of the specified stereotype for this element.
     * 
     * @param stereotype
     *            The stereotype for which to retrieve the applications.
     * @return The applications of the specified stereotype for this element, or an
     *         empty list if no such stereotype application exists.
     */
    EList<StereotypeApplication>
            getStereotypeApplications(Stereotype stereotype);

    /**
     * Retrieves the applications of the stereotype with the specified qualified
     * name for this element.
     * 
     * @param qualifiedName
     *            The qualified name of the stereotype for which to retrieve the
     *            applications.
     * @return The applications of the stereotype with the specified qualified
     *         name for this element, or empty list if no such stereotype
     *         application exists.
     */
    EList<StereotypeApplication>
            getStereotypeApplications(String qualifiedName);

    /**
     * Retrieves the stereotypes that are applied to this element.
     * 
     * @return The stereotypes that are applied to this element.
     */
    EList<Stereotype> getAppliedStereotypes();

    /**
     * Retrieves the stereotype with the specified qualified name that is
     * applied to this element.
     * 
     * @param qualifiedName
     *            The qualified name of the applied stereotype to retrieve.
     * @return The stereotype with the specified qualified name that is applied
     *         to this element, or null if no such stereotype is applied.
     */
    Stereotype getAppliedStereotype(String qualifiedName);

    /**
     * Determines whether the specified stereotype is applicable to this
     * element.
     * 
     * @param stereotype
     *            The stereotype in question.
     * @return True if the specified stereotype is applicable to this element,
     *         otherwise false.
     */
    boolean isStereotypeApplicable(Stereotype stereotype);

    /**
     * Determines whether the specified stereotype is applied to this element.
     * 
     * @param stereotype
     *            The stereotype in question.
     * @return True if the specified stereotype is applied to this element,
     *         otherwise false.
     */
    boolean isStereotypeApplied(Stereotype stereotype);

    /**
     * Applies the specified stereotype to this element.
     * 
     * @param stereotype
     *            The stereotype to apply.
     * @return The stereotype application, or null if stereotype wasn't applied
     *         successfully.
     */
    StereotypeApplication applyStereotype(Stereotype stereotype);

    /**
     * Unapplies the specified stereotype for this element by removing all
     * applications of the specified stereotype and returns these applications.
     * 
     * @param stereotype
     *            The stereotype to unapply.
     * @return The removed stereotype applications.
     */
    EList<StereotypeApplication> removeAllStereotypeApplications(
            Stereotype stereotype);

    /**
     * Removes the specified stereotype application from this element.
     * 
     * @param stereotypeApplication
     *            The stereotype application to remove.
     */
    void
            removeStereotypeApplication(
                    StereotypeApplication stereotypeApplication);

    /**
     * Retrieves the stereotypes that are applicable to this element, including
     * those that are already applied.
     * 
     * The current state of this element is considered, which means that upper
     * and lower bounds of the stereotype extensions are checked.
     * 
     * @return The stereotypes that are applicable to this element.
     */
    EList<Stereotype> getApplicableStereotypes();

    /**
     * Retrieves the stereotypes of the given profile that are applicable to
     * this element, including those that are already applied.
     * 
     * The current state of this element is considered, which means that upper
     * and lower bounds of the stereotype extensions are checked.
     * 
     * @param profile
     *            the profile for which applicable stereotypes shall be returned
     * @return The stereotypes of the given profile that are applicable to this
     *         element.
     */
    EList<Stereotype> getApplicableStereotypes(Profile profile);

    /**
     * Retrieves the stereotype with the specified qualified name that is
     * applicable to this element.
     * 
     * @param qualifiedName
     *            The qualified name of the applicable stereotype to retrieve.
     * @return The stereotype with the specified qualified name that is
     *         applicable to this element, or null if no such stereotype is
     *         applicable.
     */
    Stereotype getApplicableStereotype(String qualifiedName);

    /**
     * Retrieves all profile applications, not only for this element.
     * 
     * @return The profile applications.
     */
    EList<ProfileApplication> getAllProfileApplications();

    /**
     * Retrieves the profile applications for this element.
     * 
     * @return The profile applications for this element.
     */
    EList<ProfileApplication> getProfileApplications();

    /**
     * Saves the total profile application that contains this element to a
     * profile application file. Multiple profile applications are saved if this
     * element has stereotypes from multiple profiles applied.
     */
    void saveContainingProfileApplication();

}
