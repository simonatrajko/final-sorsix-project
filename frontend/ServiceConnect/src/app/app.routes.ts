import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { SignUpComponent } from './pages/sign-up/sign-up.component';
import { UserProfileComponent } from './pages/user-profile/user-profile.component';
import { ProvidersComponent } from './pages/providers/providers.component';
import { ServiceFormComponent } from './pages/service-form/service-form.component';
import { SeekerFormComponent } from './pages/seeker-form/seeker-form.component';
import { ProviderFromComponent } from './pages/provider-from/provider-from.component';
import { ScheduleProviderComponent } from './pages/schedule-provider/schedule-provider.component';
import { ServicesComponent } from './pages/services/services.component';
import { ProviderBookingsComponent } from './pages/provider-bookings/provider-bookings.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignUpComponent },
  {path:"user/:username",component:UserProfileComponent},
  {path:"providers",component:ProvidersComponent},
  {path:"provider/:username/add-service",component:ServiceFormComponent},
  {path:"signup/seeker",component:SeekerFormComponent},
  {path:"signup/provider",component:ProviderFromComponent},
  {path:"user/:username/schedule",component:ScheduleProviderComponent},
  {path:"user/:username/services",component:ServicesComponent},
  {path:"provider/:username/bookings",component:ProviderBookingsComponent}

];
