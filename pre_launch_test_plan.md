# Andam Davao — Pre-Launch Testing & Quality Audit Plan
**Project Goal**: Disaster preparedness and evacuation assistance mobile application for Davao City.

---

## 1. Quality Assurance Checklist Matrix

Statuses below are honest as of the last code change. Update a row to PASS only after you have actually run the check on a device.

| # | Test Area | Required Behavior | Verification Method | Status |
|---|---|---|---|---|
| **1** | **Guest Access** | Guests can view all centers, map, advisories, hotlines, and guides without signing up. | Open app fresh -> Tap "Continue as Guest" -> Navigate all screens. | NOT YET TESTED |
| **2** | **Location Permission** | Location is optional. Denying permission enables manual Barangay selection. | Deny location prompt -> Select Barangay manually from dropdown. | NOT YET TESTED |
| **3** | **Status Accuracy** | Center availability shows explicit verified status (`Open`, `Full`, `Closed`, `Status unknown`). | Verify status badges and checking authority timestamp. | NOT YET TESTED |
| **4** | **Hotline Verification** | All hotlines match current official contact numbers for Davao City. | DRRMO `(082) 241-1000`, Red Cross `(082) 226-4521`, BFP `(082) 224-4206`, PNP `(082) 222-3296`, DOH `1555`. | NOT VERIFIED (confirm with DRRMO) |
| **5** | **Hazard Overlays** | Overlays identify authoritative sources (MGB XI / PAGASA Davao River Basin). | Toggle layer checkboxes -> Inspect attribution labels. | REMOVED (no verified hazard data source yet) |
| **6** | **Firestore Security** | Guests & regular users cannot write or edit official records. | Enforced in `firestore.rules` (`isAdmin()` rule checks). | RULES DRAFTED, test with Firebase Emulator |
| **7** | **Draft Advisories** | Unapproved draft advisories are hidden from the public Android app. | Drafts have `status: 'draft'`, excluded from public query & FCM notifications. | NOT IMPLEMENTED (needs Firebase and admin portal) |
| **8** | **Admin Audit Log** | All administrator actions record UID, action, collection, and timestamp. | Test status change in Web Admin Portal -> Check Audit Log table. | NOT IMPLEMENTED (admin dashboard is a mockup) |
| **9** | **Offline Fallback** | App provides cached verified data when network connection is lost. | Turn off Wi-Fi/Data -> App renders local fallback dataset cleanly. | IMPLEMENTED, needs device test |
| **10** | **Pilot Deployment** | Supervised pilot review with local disaster management offices before public release. | Conduct pilot testing with Davao City DRRMO representatives. | PENDING |

---

## 2. Low-Connectivity & Offline Testing Procedure

1. **Network Disconnection Test**:
   * Put the device in **Airplane Mode** or disable mobile data & Wi-Fi.
   * Launch **Andam Davao**.
   * Observe that the app gracefully loads the cached verified evacuation centers, emergency hotlines, and preparedness guides without crashing.
2. **Re-connection Sync Test**:
   * Re-enable network connectivity.
   * Repositories automatically re-fetch updated public advisories from Cloud Firestore.

---

## 3. Pre-Launch Partner Review Protocol

Before publishing to the Google Play Store:
1. Validate every evacuation center coordinate and contact number with the **Davao City Disaster Risk Reduction and Management Office (DRRMO)**.
2. Ensure project disclaimers state that the app complements, but does not replace, official emergency broadcasts from DRRMO or local authorities.
3. Configure Google Maps API Key restrictions in Google Cloud Console.
