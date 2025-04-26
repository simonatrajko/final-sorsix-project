import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LoginService } from '../../services/login.service';
import { User } from '../../models/User';
@Component({
  selector: 'app-header',
  imports: [RouterLink],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  currentUser: User | null = null;

  constructor(private loginLogoutService: LoginService) {
    this.loginLogoutService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });
  }

  handleLogout() {
    this.loginLogoutService.handleLogout();
  }
}
