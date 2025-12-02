## [0.2.0] - 01/12/2025
### Added
- Modify beneficiary

### Fixed
- Debug modify beneficiary

## [0.2.0] - 30/11/2025
### Added
- feature for scanning qr code for attendance

### Changed
- Added a wey to prevent certain chars and emojis in workshop inputs. 

### Fixed
- Fixed attendance date/time formatting.
- Improved "View Attendance" button alignment and styling.

## [0.2.0] - 29/11/2025
### Added
- Added offline for product batch list and detail
- Added a Capacitations view for Disabilities List
- Added offline for supply batches

## [0.2.0] - 29/11/2025

### Added
- RBAC for coordinator, assistant, and scholar roles
- Atom for Multiple selects

### Changed
- Change beneficiary detail for showing more disabilities
- Change so now Beneficiaries can be registered with more disabilities

## [0.1.0] - 25/11/2025
### Added
- US for consult attendance

## [0.1.0] - 20/11/2025
### Added
- ModifyProduct validation fields
- RegisterProduct validation fields
- Arrow to return register and modify product
- US for filter and order workshops.
- Functional go back button to Supplies List Screen
- Functional go back buttons to Orders and Inventory Screens

### Deleted
- All the NotificationIcon buttons of the screens
- Validations for supply add and update
- Scroll in detail view of a supply
- Scroll for login, home and inventory screens.

### Fixed
- Fix beneficiary list navbar
- Fix workshops date input
- Fix workshops hour input
- Fix Workshops navbar
- Fix beneficiary registration navBar appearing
- Fix view after deleting a beneficiary
- Fix date input
- Fix scroll bar for supply batch views
- Input Validations
- Fix view to not show the navbar in beneficiary details
- Fix beneficiary list navbar
- Fix the go back buttons of Products, Product Batches and Supplies to be the same
- Fix the go back button of teh Beneficiary Screen to be the same icon of the Workshop screen
- Fix data load on modify product batch screen
- Fix expiration date availabity for null values
- Fix modify workshops for date and hour
- Fix workshops scroll and notifications
- Fixed Date on Register Beneficiary now using the calendar
- Return button on Register Beneficiary
- Errors now indicate whats wrong on Register beneficiary

## [0.1.0] - 19/11/2025
### Added
 - Added modify for workshops.
 - Supply module conected to the inventory screen.
 - Added modify for workshops.
 - US for modify supply batch
 - US for display of supply batches based on date and idSupply
 - US for modify User with MVVM and clean architecture principles.
 - US for qr generation
 - Navigation to ProductBatches

### Fixed
 - buttons from product detail screen and navigation for products
 - Return Icon from profuct list screen to the navigation of inventory

### Fixed
- All product info for modify necessary
- All product info necesary
- Product add necesary image
- Fixed search bar in workshop list.
- Fixed the product error message
- Error now properly appearing when registering new beneficiary
- Error in descriptcion in view and modify a workshop
- Fixed error views in product batches list, detail, register and modify
- Fixed product fetch in register Product Batch view
- Fixed of list update when deleting product

## [0.1.0] - 18/11/2025

### Added
- ProductList screen to navgraph
- Modify product implementation

### Fixed
- Fixed the possibility to add or modify one supply with the same name
- Fixed the date format in supplyDetailContent screen
- Fixed error in register workshop
- Fixed error in view Workshops after a delete

### Added
- US for RBAC main screens and navigation accross the app
- Added filter and order supply batches

## [0.1.0] - 17/11/2025

### Changed
 - UploadImageInput modified to not save persistantly the photo, instead to 
   receive the URI value from the view model  

## [0.1.0] - 17/11/2025

- US for modify Supply with MVVM and clean architecture principles
- Delete supply batch
- Added filter and order products
- Added filter and order product batches
- Added functional product batch search bar
- Added register Beneficiary View
- Added Consult Disabilities List

## [0.1.0] - 16/11/2025

### Added
- Added products search
- Add DeleteWorkshop Logic, Data and Domain
- Add way to get users for workshops
- Added functionality search beneficiaries
- Added functionality view a Workshop (Video functionality needs more info)
- Filter and order beneficiaries functionality
- Fixed merge conflicts
- Added US to Delete Product Batches

### Fixed
- Refreshing on Product Batches List

## [0.1.0] - 15/11/2025
### Fixed

### Fixed
- Fixed merge conflicts with products

## [0.1.0] - 15/11/2025

### Added
- Added function to the delete products button
- Added US to Register Product Batches

### Fixed
- Fixed StandardInput missing parameter
- Fixed error from standard input attibutes into register supply batch screen

## [0.1.0] - 14/11/2025

### Fixed
 - Information fetched from the backend for consultBeneficiaries

### Added
 - Added logic for consultBeneficiaries
 - Added on detailBeneficiary so that it receives a URL as image from the backend and puts it on display

## [0.1.0] - 13/11/2025

### Added
- RegisterProduct screen implementation.
- Added Workshop Card atom.
- Added screen for view Workshop 
- Added function for searching workshops by name.
- Added US to Register Product Batches

### Fixed
- ArcaApi get all external collabs
- Search users
- Added presentation, data and domain for RegisterSupplyBatch
- Fix snackbar for register new workshops
- Fix error of implementation in Input atoms

## [0.1.0] - 12/11/2025

### Added
- Consult Product Batches + Register ViewModel and Screen
- DeleteOneSupply Button Logic
- Filter and search bar for users
- US to add a new supply with an image
- Included internal collaborators in the area of users

### Updated
- External collabs screen to user general screen
- External register screen to user register screen


## [0.1.0] - 11/11/2025

### Fixed
 -  Fixed Main Activity for changes made for supply

### Added
- Added final comments to the function delete beneficiaries
- Consulting an specific Supply with the batches it is contained into.
- Added customization of Snackbar Atom

## [0.1.0] - 10/11/2025

### Fixed

 - BeneficiaryDetail.kt fixed error that prevented merge
 - Fixed the validation forms to register new workshop for hour and date
 - Fixed the save confirmation with the desition dialog.
 - Fixing validation in register new workshops.


### Added
 - Added delete users feature
 - Added basic logic for US BEN-01 and BEN-02
 - Added DecisionDialog Atom
 - Screen to consult details of external collaborators
 - Screen to consult list of external collaborators
 - Screen to register external collaborators
 - In register new workshops the snakbar was added.

## [0.1.0] - 9/11/2025

### Fixed
 - Modify button
 - The colors of upload image, icon standar input and select option input, to match the standard input

### Added
 - Added Screen for Beneficiary Details
 - The files that get to be with the Api related to Workshops
 - The view model, the view, the model for register new workshop.
 - routes in ArcaApi and AppModule to work with workshops.

### Updated
 - Error handling for password recovery feature

## [0.1.0] - 8/11/2025

### Fixed
 - Add button

### Added
 - Search bar and filter icon.
 - Supplies list documentation for the code.

### Updated
 - Login UI and RBAC roles

## [0.1.0] - 7/11/2025

### Fixed
 - Fixed some unused imports that were on files such as passwordRecovery screen and the use of proper atoms, that disallowed the building of the proyect.

### Added
 - Added Screen for Beneficiary List
 - Added square buttons for + and
 - Added atom for description
## [0.1.0] - 6/11/2025

### Fixed
- Fixed the size of the text of 'Beneficiarios' so that it works on even smaller android devices
- Fixed the use of fonts of the beneficiary cards

### Added
- Added molecule for beneficiary cards
- Password recovery and account activation with token verification

## [0.1.0] - 5/11/2025

### Updated
 - Login and Splash Screen UI

### Added
- Added atom for selection
- Added atom for date input

## [0.1.0] - 4/11/2025

### Added

 - Added supplies list functionality with all the MVVM layers
 - Added atom for password input
 - Validation input function ValidatePassword created
 - Added atom for upload images
 - Added atom for email input
 - Fixed email regex validation
 - Validation input function ValidateEmail created

## [0.1.0] - 3/11/2025

### Added

 - Added molecule for NavBar
 - Added atom for apply button 
 - Added atom for delete all button

## [0.1.0] - 9/10/2025

### Added

 - Added atom for confirm button 
 - Added atom for modify button 
 - Added atom for adding in white color ('+')
 - Added atom for standard input
 - Added atoms for headlines Large and Medium (25/10/2025)
 - Added atoms for body Small, Medium and Large (26/10/2025)
 - Poppins Font added in app/res/font package (24/10/2025)
 - Compose function SaveButton created (24/10/2025)
 - Typography file in presentation/theme created to use the poppins (24/10/2025)
 - Atomic composable icons for UI (23/10/2025)
 - Atomic composable status for UI (27/10/2025)

### Change
- Modify constructor parameters of savebutton



## [0.0.0] - 9/10/2025

### Added

 - Repo structure of UI folder (components, atoms, molecules, organisms) (22/10/2025)
 - To the folders included in this commit there are files that are name realted to 'Example' to modify them in the future (22/10/2025)
 - Coding Standard (21/10/2025)
 - Changelog file (21/10/2025)
 - Gitignore file (14/10/2025)
 - Basic structure of android studio (13/10/2025)
 - Pull request template file (13/10/2025)
