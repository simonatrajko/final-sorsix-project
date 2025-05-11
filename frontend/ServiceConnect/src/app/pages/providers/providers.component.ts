import { Component } from '@angular/core';
import { ProviderCardComponent } from '../../components/provider-card/provider-card.component';
import { ProviderDTO } from '../../models/ProviderDTO';
import { ProviderService } from '../../services/provider.service';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs';
import { RouterLink } from '@angular/router';
@Component({
  selector: 'app-providers',
  imports: [ProviderCardComponent,ReactiveFormsModule,RouterLink],
  templateUrl: './providers.component.html',
  styleUrl: './providers.component.css'
})
export class ProvidersComponent {
  providers:ProviderDTO[]=[];
  searchControl=new FormControl()
  constructor(private providerService:ProviderService) {
    
  }
  
  ngOnInit(){
    this.searchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(name=>this.providerService.searchProviders(name))
    ).subscribe(res=>{
      this.providers=res
      console.log(this.providers)
    })
  }
  
}
