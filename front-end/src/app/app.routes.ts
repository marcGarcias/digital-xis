import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { PrivacyPolicy } from './pages/privacy-policy/privacy-policy';

export const routes: Routes = [
  {
    path: '',
    component: Home,
  },
  {
    path: 'politica-de-privacidade',
    component: PrivacyPolicy,
  }
];