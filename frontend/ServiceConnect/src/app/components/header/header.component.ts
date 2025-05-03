import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { User } from '../../models/User';
import { UserService } from '../../services/user.service';
@Component({
  selector: 'app-header',
  imports: [RouterLink],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  currentUser: User | null = null;

  constructor(private loginLogoutService: UserService) {
    this.loginLogoutService.currentUser$.subscribe((user) => {
      this.currentUser = user;
    });
  }

  handleLogout() {
    this.loginLogoutService.handleLogout();
  }
}
