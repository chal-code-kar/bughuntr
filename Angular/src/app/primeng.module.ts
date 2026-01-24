import {ButtonModule} from 'primeng/button';
import {PanelModule} from 'primeng/panel';
import {CardModule} from 'primeng/card';
import {MessageService} from 'primeng/api';
import { NgModule } from '@angular/core';
import { DialogService, DynamicDialogModule } from 'primeng/dynamicdialog';
import {TableModule} from 'primeng/table';
import {InputTextareaModule} from 'primeng/inputtextarea';
import { InputTextModule } from 'primeng/inputtext';
import {CalendarModule} from 'primeng/calendar';
import {MultiSelectModule} from 'primeng/multiselect';
import {FileUploadModule} from 'primeng/fileupload';
import {ConfirmationService} from 'primeng/api';
import {StepsModule} from 'primeng/steps';
import {CheckboxModule} from 'primeng/checkbox';
import { InputNumberModule } from 'primeng/inputnumber';
import { StyleClassModule } from 'primeng/styleclass';
import { TooltipModule } from 'primeng/tooltip';
import { ChipsModule } from 'primeng/chips';
import { AvatarGroupModule } from 'primeng/avatargroup';
import { AvatarModule } from 'primeng/avatar';
import { EditorModule } from 'primeng/editor';
import { PaginatorModule } from 'primeng/paginator';
import { TagModule } from 'primeng/tag';
import { RippleModule } from 'primeng/ripple';
import {KeyFilterModule} from 'primeng/keyfilter';
import {ToastModule} from 'primeng/toast';
import {DividerModule} from 'primeng/divider';
import {InputSwitchModule} from 'primeng/inputswitch';
import {OverlayPanelModule} from 'primeng/overlaypanel';
import {CarouselModule} from 'primeng/carousel';
import {DialogModule} from 'primeng/dialog';
import {BreadcrumbModule} from 'primeng/breadcrumb';
import {PickListModule} from 'primeng/picklist';
import {DropdownModule} from 'primeng/dropdown';
import {ConfirmPopupModule} from 'primeng/confirmpopup';
import {TriStateCheckboxModule} from 'primeng/tristatecheckbox';
import {ProgressSpinnerModule} from 'primeng/progressspinner';
import {TabViewModule} from 'primeng/tabview';
import {KnobModule} from 'primeng/knob';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import {MenuModule} from 'primeng/menu';
import {AccordionModule} from 'primeng/accordion';

@NgModule({
  exports: [
    ButtonModule,
    RippleModule,
    TagModule,
    PaginatorModule,
    EditorModule,
    CardModule,
    StepsModule,
    TableModule,
    InputTextModule,
    CalendarModule,
    InputNumberModule,
    InputTextareaModule,
    AvatarModule,
    AvatarGroupModule,
    StyleClassModule,
    MultiSelectModule,
    FileUploadModule,
    PanelModule,
    TooltipModule,
    CheckboxModule,
    KeyFilterModule,
    ToastModule,
    DividerModule,
    InputSwitchModule,
    OverlayPanelModule,
    CarouselModule,
    DialogModule,
    BreadcrumbModule,
    PickListModule,
    DropdownModule,
    DynamicDialogModule,
    ChipsModule,
    ConfirmPopupModule,
    TriStateCheckboxModule,
    ProgressSpinnerModule,
    TabViewModule,
    KnobModule,
    ConfirmDialogModule,
    MenuModule,
    AccordionModule

   ],
   providers: [MessageService,DialogService,ConfirmationService],
   imports:[]
})
export class PrimeNgModule { }

