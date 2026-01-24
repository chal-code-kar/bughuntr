import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SmconfigErrorComponent } from './smconfig-error/smconfig-error.component';
import { BbComponent } from './bb/bb.component';
import { HelpComponent } from './help/help.component';
import { AddhelpComponent } from './addhelp/addhelp.component';
import { RprofileComponent } from './rprofile/rprofile.component';
import { BountyProgramComponent } from './bounty-program/bounty-program.component';
import { BbprogdetlsComponent } from './bbprogdetls/bbprogdetls.component';
import { AddResearcherComponent } from './add-researcher/add-researcher.component';
import { EditresearcherComponent } from './editresearcher/editresearcher.component';
import { ReportVulnerabilityComponent } from './report-vulnerability/report-vulnerability.component';
import { EditVulnerabilityComponent } from './edit-vulnerability/edit-vulnerability.component';
import { ViewAllVulnerabilityComponent } from './view-all-vulnerability/view-all-vulnerability.component';
import { ViewVulnerabilityComponent } from './view-vulnerability/view-vulnerability.component';
import { ViewResearcherComponent } from './view-researcher/view-researcher.component';
import { EditprogrammeComponent } from './editprogramme/editprogramme.component';
import { CreatenewprogrammeComponent } from './createnewprogramme/createnewprogramme.component';
import { RbacComponent } from './rbac/rbac.component';
import { ResourcesComponent } from './resources/resources.component';
import { AdminComponent } from './admin/admin.component';
import { WorklistComponent } from './worklist/worklist.component';
import { AccessDeniedComponent } from './error/access-denied/access-denied.component';
import { ReportsComponent } from './reports/reports.component';
import { AddUserComponent } from './add-user/add-user.component';
import { BlogsComponent } from './blogs/blogs.component';
import { BlogMenuComponent } from './blog-menu/blog-menu.component';
import { HelpUpdateComponent } from './help-update/help-update.component';
import { UserRoleComponent } from './user-role/user-role.component';
import { GetQueryComponent } from './get-query/get-query.component';
import { AllResourcesComponent } from './all-resources/all-resources.component';

import { AddResourcesComponent } from './add-resources/add-resources.component';
import { AssignRoleComponent } from './assign-role/assign-role.component';
import { HelpEditComponent } from './help-edit/help-edit.component';
import { ShowqueryComponent } from './showquery/showquery.component';
import { FaqGetallComponent } from './faq-getall/faq-getall.component';
import { FaqInsertComponent } from './faq-insert/faq-insert.component';
import { ManageGetalldropdownComponent } from './manage-getalldropdown/manage-getalldropdown.component';
import { ManageEditDropdownComponent } from './manage-edit-dropdown/manage-edit-dropdown.component';
import { ManageAddDropdownComponent } from './manage-add-dropdown/manage-add-dropdown.component';
import { MenuTableComponent } from './menu-table/menu-table.component';
import { AddMenuComponent } from './add-menu/add-menu.component';
import { HistoryComponent } from './history/history.component';
import { HistoryInsertComponent } from './history-insert/history-insert.component';
import { EditHistoryComponent } from './edit-history/edit-history.component';
import { AdminHistoryComponent } from './admin-history/admin-history.component';
import { DropDownTableComponent } from './drop-down-table/drop-down-table.component';
import { AdminLeaderboardComponent } from './admin-leaderboard/admin-leaderboard.component';
import { LeaderboardComponent } from './leaderboard/leaderboard.component';
import { ETLDataComponent } from './etldata/etldata.component';

import { AddEtldataComponent } from './add-etldata/add-etldata.component';
import { AdminAuthGuard } from './admin-auth-guard.service';
import { ResourcesEditcategoryComponent } from './resources-editcategory/resources-editcategory.component';
import { LoginComponent } from './login/login.component';
const routes: Routes = [
  { path: 'error', component: SmconfigErrorComponent },
  { path: '', component: BbComponent },
  { path: 'Help', component: HelpComponent },
  { path: 'Add-Help', component: AddhelpComponent },
  {
    path: 'app-rprofile',
    component: RprofileComponent
  },
  {
    path: 'ProgramDetails',
    component: BountyProgramComponent
  },
  {
    path: 'project/:srno',
    component: BbprogdetlsComponent
  },
  { 
    path: 'AddResearcher',
    component: AddResearcherComponent
  },
  {
    path: 'editResearcher/:srno',
    component: EditresearcherComponent
  },
  { 
    path: 'ReportVulnerability/:projectid',
    component: ReportVulnerabilityComponent
  },
  { 
    path: 'EditVulnerability/:srno',
    component: EditVulnerabilityComponent
  },
  {
    path: 'ViewVulnerabilities/:projectid',
    component: ViewAllVulnerabilityComponent
  },
  { 
    path: 'ViewVulnerability/:srno',
    component: ViewVulnerabilityComponent
  },
  {
    path: 'Profile/:srno',
    component: ViewResearcherComponent
  },
  { 
    path: 'EditProgramme/:srno',
    component: EditprogrammeComponent
  },
  { 
    path: 'create',
    component: CreatenewprogrammeComponent
  },
  { 
    path: 'app-rbac',
    component: RbacComponent
  },
  {
    path: 'Resources/:srno',
    component: ResourcesComponent
  },
  {
    path: 'Resources',
    component: ResourcesComponent
  },
  { 
    path: 'Add-Help',
    component: AddhelpComponent
  },
  {
    path: 'admin',
    component: AdminComponent
  },
  {
    path: 'worklist',
    component: WorklistComponent
  },
  
  
  
  
  {
    path: '403',
    component: AccessDeniedComponent
  },
  {
    path: 'reports',
    component: ReportsComponent
  },
  {
    path: 'addUser',
    component: AddUserComponent
  },
  {
    path: 'blogs',
    component: BlogsComponent
  },
  {
    path: 'blog/:srno',
    component: HelpUpdateComponent
  },
  {
    path: 'blogMenu',
    component: BlogMenuComponent
  },
  {
    path: 'assign-roles',
    component: AssignRoleComponent
  },
  {
    path: 'app-help',
    component: HelpEditComponent
  },
  {
    path: 'blogs',
    component: BlogsComponent
  },
  {
    path: 'blog/:srno',
    component: HelpUpdateComponent
  },
  {
    path: 'blogMenu',
    component: BlogMenuComponent
  },
  {
    path: 'assign-roles',
    component: AssignRoleComponent
  },
  {
    path: 'add-resources',
    component: AddResourcesComponent
  },
  {
    path: 'edit-resources/:id',
    component: ResourcesEditcategoryComponent
  },
  {
    path: 'all-resources',
    component: AllResourcesComponent
  },
  {
    path: 'getquery',
    component: GetQueryComponent
  },
  {
    path: 'showquery',
    component: ShowqueryComponent
  },
  {
    path: 'addUser',
    component: AddUserComponent
  },
  {
    path: 'UserRole',
    component: UserRoleComponent
  },
  {
    path: 'faq-insert',
    component: FaqInsertComponent
  },
  {
    path: 'faq-getall',
    component: FaqGetallComponent
  }, {
    path: 'manage-add',
    component: ManageAddDropdownComponent
  }, {
    path: 'manage-getall',
    component: ManageGetalldropdownComponent
  }, {
    path: 'manage-edit/:id',
    component: ManageEditDropdownComponent
  }, {
    path: 'menus',
    component: MenuTableComponent
  }, {
    path: 'addMenu',
    component: AddMenuComponent
  }, {
    path: 'History',
    component: HistoryComponent
  },
  {
    path: 'history-insert',
    component: HistoryInsertComponent
  },
  {
    path: 'history-update/:srno',
    component: EditHistoryComponent
  },
  {
    path: 'admin-history',
    component: AdminHistoryComponent
    
  },
  {
    path: 'setDropdown',
    component: DropDownTableComponent
  },
  {
    path: 'researcher-details',
    component: AdminLeaderboardComponent
  },
  {
    path: 'leaderboard',
    component: LeaderboardComponent
  },
  {

    path: 'etl-data',

    component: ETLDataComponent

  },

  {

    path: 'add-etl',

    component: AddEtldataComponent

  },
  {
    path: 'login',
    component: LoginComponent
  },
  { path: '**', redirectTo: '/' },
];

@NgModule({
  
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

