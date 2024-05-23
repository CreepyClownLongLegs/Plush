import {Component, Input, OnInit} from '@angular/core';
import {RouterLink} from "@angular/router";
import {PlushToyDetailDto, PlushToyListDto} from "../../dtos/plushtoy";

@Component({
    selector: 'app-card',
    standalone: true,
    imports: [
        RouterLink
    ],
    templateUrl: './card.component.html',
    styleUrl: './card.component.scss'
})
export class CardComponent implements OnInit {

    @Input() plushie: PlushToyListDto;

    ngOnInit(): void {
    }
}
