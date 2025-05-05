import { Routes } from '@angular/router';
import { LoginComponent } from '../components/login/login.component';
import { RegisterComponent } from '../components/register/register.component';

export const routes: Routes = [
  {path:'login', component: LoginComponent},
  // { path: 'login', loadComponent: () => import('../components/login/login.component').then(m => m.LoginComponent) },
  {path: 'register', component: RegisterComponent},
];


