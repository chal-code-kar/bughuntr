import { AdminAuthGuard } from './admin-auth-guard.service';
import { ApplicationErrorHandler } from './ApplicationErrorHandler';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule, ErrorHandler, APP_INITIALIZER } from '@angular/core';
import { HTTP_INTERCEPTORS, HttpClientModule, HttpClientXsrfModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { MenusComponent } from './menus/menus.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AuthInterceptor } from './auth-interceptor';
import { SmconfigErrorComponent } from './smconfig-error/smconfig-error.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { Globals } from './globals';
import { IndexService } from './index.service';
import { CookieService } from 'ngx-cookie-service';
import { HelpComponent } from './help/help.component';
import { AddhelpComponent } from './addhelp/addhelp.component';
import { TssvcommonvalidatorService } from './services/tssvcommonvalidator.service';
import { PrimeNgModule } from './primeng.module';
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
import { ErrorComponent } from './error/error/error.component';
import { AccessDeniedComponent } from './error/access-denied/access-denied.component';
import { ReportsComponent } from './reports/reports.component';
import { RouterModule } from '@angular/router';
import { DatePipe, HashLocationStrategy, LocationStrategy } from '@angular/common';
import { BbComponent } from './bb/bb.component';
import { AddUserComponent } from './add-user/add-user.component';
import { HighchartsChartModule } from 'highcharts-angular';
import { BlogsComponent } from './blogs/blogs.component';
import { HelpUpdateComponent } from './help-update/help-update.component';
import { BlogMenuComponent } from './blog-menu/blog-menu.component';
import { AllResourcesComponent } from './all-resources/all-resources.component';
import { AddResourcesComponent } from './add-resources/add-resources.component';
import { GetQueryComponent } from './get-query/get-query.component';
import { ShowqueryComponent } from './showquery/showquery.component';
import { UserRoleComponent } from './user-role/user-role.component';
import { AssignRoleComponent } from './assign-role/assign-role.component';
import { HelpEditComponent } from './help-edit/help-edit.component';
import { FaqInsertComponent } from'./faq-insert/faq-insert.component';
import { FaqGetallComponent }from'./faq-getall/faq-getall.component';
import { ManageAddDropdownComponent } from './manage-add-dropdown/manage-add-dropdown.component';
import { ManageEditDropdownComponent } from './manage-edit-dropdown/manage-edit-dropdown.component';
import { ManageGetalldropdownComponent } from './manage-getalldropdown/manage-getalldropdown.component';
import { AddMenuComponent } from './add-menu/add-menu.component';
import { MenuTableComponent } from './menu-table/menu-table.component';

import { markedOptionsFactory } from './markdown';
import { HistoryComponent } from './history/history.component';
import { HistoryInsertComponent } from './history-insert/history-insert.component';
import { EditHistoryComponent } from './edit-history/edit-history.component';
import { AdminHistoryComponent } from './admin-history/admin-history.component';
import { DropDownTableComponent } from './drop-down-table/drop-down-table.component';
import { AdminLeaderboardComponent } from './admin-leaderboard/admin-leaderboard.component';
import { LeaderboardComponent } from './leaderboard/leaderboard.component';
import { ETLDataComponent } from './etldata/etldata.component';

import { AddEtldataComponent } from './add-etldata/add-etldata.component';
import { CustomDomSharedStylesHost } from './shared_styles_host';
import { CustomMediaMatcher } from './media-matcher';
import { MediaMatcher } from '@angular/cdk/layout';
import { MarkdownModule, MarkedOptions } from 'ngx-markdown';
import { ResourcesEditcategoryComponent } from './resources-editcategory/resources-editcategory.component';
import { AuthInitService } from './auth-init.service';



@NgModule({
  declarations: [
    AppComponent,
    MenusComponent,
    SmconfigErrorComponent,
    HeaderComponent,
    FooterComponent,
    BbComponent,
    HelpComponent,
    AddhelpComponent,
    RprofileComponent,
    WorklistComponent,
    BountyProgramComponent,
    BbprogdetlsComponent,
    AddResearcherComponent,
    EditresearcherComponent,
    ReportVulnerabilityComponent,
    EditVulnerabilityComponent,
    ViewAllVulnerabilityComponent,
    ViewVulnerabilityComponent,
    ViewResearcherComponent,
    EditprogrammeComponent,
    CreatenewprogrammeComponent,
    RbacComponent,
    ResourcesComponent,
    AdminComponent,
    ErrorComponent,
    AccessDeniedComponent,
    ReportsComponent,
    AddUserComponent,
    BlogsComponent,
    BlogMenuComponent,
    HelpUpdateComponent,
    AllResourcesComponent,
    AddResourcesComponent,
    ResourcesEditcategoryComponent,
    
    GetQueryComponent,
    ShowqueryComponent,
    UserRoleComponent,
    AssignRoleComponent,
    HelpEditComponent,
    FaqInsertComponent,
    FaqGetallComponent,
    ManageAddDropdownComponent,
    ManageEditDropdownComponent,
    ManageGetalldropdownComponent,
    AddMenuComponent,
    MenuTableComponent,
    HistoryComponent,
    HistoryInsertComponent,
    AdminHistoryComponent,
    EditHistoryComponent,
    DropDownTableComponent,
    AdminHistoryComponent,
    AdminLeaderboardComponent,
    LeaderboardComponent,
    ETLDataComponent,
    AddEtldataComponent
  ],
  imports: [

    BrowserModule,
    FormsModule,
    HttpClientModule,
    HttpClientXsrfModule.withOptions({
      cookieName: 'XSRF-TOKEN',
      headerName: 'X-XSRF-TOKEN'
    }),
    AppRoutingModule,
    BrowserAnimationsModule,
    PrimeNgModule,
    RouterModule,
    HighchartsChartModule,

    MarkdownModule.forRoot({

      markedOptions: {

        provide:{} as MarkedOptions,

        useFactory: markedOptionsFactory,

      },

    })

  ],
  
  providers: [
    
     {
      provide: APP_INITIALIZER,
      useFactory: (authInit: AuthInitService) => () => authInit.loadRole(),
      deps: [AuthInitService],
      multi: true
    },
    
    
    {
      provide: ErrorHandler,
      useClass: ApplicationErrorHandler
    },
    ApplicationErrorHandler,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    },

    { provide: 'cspMetaSelector', useValue: 'meta[name="CSP-NONCE"]' },

    { provide: MediaMatcher, useClass: CustomMediaMatcher},
    
    IndexService,
    Globals,
    AdminAuthGuard,
    CookieService,
    TssvcommonvalidatorService,
    DatePipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
