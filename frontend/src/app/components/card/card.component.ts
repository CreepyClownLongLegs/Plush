import {Component, Input, OnInit} from '@angular/core';
import {PlushToyListDto} from "../../dtos/plushtoy";
import {ButtonType} from "../login/login.component";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrl: './card.component.scss'
})

export class CardComponent implements OnInit {

  @Input() plushie: PlushToyListDto;
  @Input() imageUrl!: string;

  constructor(
    public authService: AuthService,
  ) {
  }

  ngOnInit(): void {
  }

  protected readonly ButtonType = ButtonType;
}
