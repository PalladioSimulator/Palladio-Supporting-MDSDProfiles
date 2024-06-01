# Palladio MDSD Profiles
MDSD Profiles enable profile and stereotype support to metamodels. For this support, MDSD Profiles provide two metaclasses, ProfilableElement and StereotypableElement, from which metaclasses can inherit. ProfilableElements allow to reference a given profile; the ProfileableElement is typically the root node of a model. If such a root node accordingly references a profile, Stereotypes of the referenced profile can be applied to any StereotypableElement within that root node. Note that MDSD Profiles is based on an extension to the [EMF Profiles Project](http://www.modelversioning.org/emf-profiles).

## Setting Up MDSD Profiles
### Installation: Update Sites
The MDSD Profiles API is integrated into Palladio and currently part of the Palladio nightly build.
* If you want to use MDSD Profiles together with Palladio, install Palladio using the update site of the nightly build.
* If you want to use MDSD Profiles without Palladio, just install it from the MDSD Profiles update site
* If you want to create new profiles and not only use existing ones you have to install the EMF Profiles feature from the EMF Profiles update site.
    * It is strongly advised, that you install the new Sirius-based Profile Editor nightly update site.
### Profile and Stereotype Definition
> [!NOTE]
> If somebody else created a profile that you only want to use by applying its stereotype, then you don't have to read this.
* How stereotypes and profiles can be defined using EMF Profiles is explained on a separate page.
### Non-Invasive Use: Using the MDSD Profile API
If you want to use MDSD Profiles within code, you should use the static methods from the MDSD Profile API (plug-in "org.palladiosimulator.mdsdprofiles.api"). You can easily apply, unapply, query, update, etc. profiles, stereotypes, and tagged values for any Resource and EObject.
### Invasive Use: Making Models Extendable
> [!WARNING]
> Going the invasive way means enriching your metamodel with MDSDProfiles. Do that only if you have good reasons for it (e.g., you need MDSD Profile operations directly within your EObject). Instead, the preferred way would be the non-invasive API usage (see above).

> [!NOTE]
> If you are applying stereotypes to models for which somebody else made already sure that they can be extended, then you do not need to read this.

To extend instances of a metamodel using stereotypes of a profile, you have to enrich your metamodel by (1) ProfilableElements that allow to reference a given profile; the ProvileableElement is typically the root node of a model and (2) StereotypableElements to which then stereotypes of the referenced profile can be applied. For such an enrichment, you have to inherit from ProfilableElement respectively from StereotypableElement.

### Enabling Tree Editor Support
> [!NOTE]
> If profile support has already been integrated into your tree editor, then you do not need to read this.
Use the `StereotypableElementDecoratorAdapterFactory` to decorate your `ItemProviderAdapterFactory` of the generated `*.editor` plugin. Do not forget to mark your code adaptation with `@generated NOT`. For example, your adapatation could look like this:
```
this.adapterFactory = new StereotypableElementDecoratorAdapterFactory(new YourItemProviderAdapterFactory(compAdapterFactory));
```

## Using MDSD Profiles
Ensure that you have a profile definition available within your workspace. Then, you can use different editors to work with MDSD Profiles.

### Tree Editors
* Profiles: right-click on a ProfilableElement and choose "MDSD Profiles... -> Apply/Unapply Profiles"
* Stereotypes: right-click on a StereotypableElement and choose "MDSD Profiles... -> Apply/Unapply Stereotypes"
* Tagged Values: simply use the property view to edit tagged values of elements with applied stereotypes
### GMF-based Editors
No support yet.

### Sirius-based Editors
No support yet.

## Contributing to the Development of MDSD Profiles
### Requirements to Profiles
We collected several requirements we have to profiles. In any development, obey that these are maintained:

* Performance: Profiles should not slow-down the normal usage of the Eclipse workbench
* Headless: Stereotypes of model elements should also be accessible without IDE
* Blackboard support: When loading models with stereotypes, stereotypes need also to be loaded to the blackboard
* Query applicable/applied profiles: For a given (profileable) model element, there must be queries that return applicable respectively applied profiles of this element
* Query applicable/applied stereotypes: For a given (stereotypable) model element, there must be queries that return applicable respectively applied stereotypes of this element
* Read/Write applied stereotypes: Operations for accessing and altering applied stereotypes need to be available
* Read/Write applied tagged values: Operations for accessing and altering the values of attributes of applied stereotypes (tagged values) need to be available
* Graphical editor support: we need editor support for (1) generic tree editors & property sheets, (2) old GMF-based editors, and (3) within new Sirius-based editors
* Libraries for QVTo, QVTr, ...: We need to provide libraries to transformation languages that allow to easily work with profiles
* Model always loadable: Models with applied profiles/stereotypes must be able to be successfully loaded even when the applied profile is unknown (e.g., when no global * * profile registry is available)

We explicitly violate the following requirements for the given reasons:
* Profiled metamodel unchanged: We require to change metamodel where profiles should be applied to. For example, in the PCM we made Entities Stereotypable. We did so to comply to some of above requirement ("Blackboard support" and "Model always loadable")

### Bug Reports
Please report bugs using [GitHub issues](https://github.com/PalladioSimulator/Palladio-Supporting-MDSDProfiles/issues)

### Build and Tests
For Information on builds and tests of MDSD Profiles go to the [GitHub actions page](https://github.com/PalladioSimulator/Palladio-Supporting-MDSDProfiles/actions)

### Setting up an Eclipse for the Development of MDSD Profiles
To set up a development environment for MDSD Profiles in a Eclipse bench you need the according EMF Profiles and PCM Profiles plug-ins:

* Eclipse Setup
    * Download and install Eclipse Modeling SR1 (Luna is best, Kepler also worked)
    * Install an SVN-Team Provider (and SVN-Connector)
    * For Kepler and older Eclipse versions: Install the Log4j OSGi Bundle
        * Update-Site: http://download.eclipse.org/tools/orbit/downloads/drops/R20130827064939/repository
        * Choose "Apache Jakarta Log4j Version 1.2.15"
    * Install Graphiti (using Help -> Install Modelling Components, the decorator.graphiti plug-in of EMF Profiles depends on it)
* Get Sources for Profiles
    * Clone the EMF Profiles GIT Master branch: https://code.google.com/a/eclipselabs.org/p/emf-profiles/ and import all its projects.
    * Clone the current repository
* Run
    * a new eclipse instance with all EMF Profiles and PCM Profiles projects

## Support
For support
* visit our [issue tracking system](https://palladio-simulator.com/jira)
* contact us via our [mailing list](https://lists.ira.uni-karlsruhe.de/mailman/listinfo/palladio-dev)

For professional support, please fill in our [contact form](http://www.palladio-simulator.com/about_palladio/support/).
