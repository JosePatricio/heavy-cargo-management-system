import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {PublicService} from "../../services/public.service";
import { Subject } from 'rxjs/Subject';
import { timer } from 'rxjs/observable/timer';
import { switchMap, startWith, scan, takeWhile, takeUntil, mapTo } from 'rxjs/operators';

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})
export class IndexComponent implements OnInit {

  public ahorroGeneradoContador =  76329;
  public toneladasTrasladadasContador = 5333;
  public destinosContador = 73;

  private ahorroGenerado = 0;
  private toneladasTrasladadas = 0;
  private destinos = 0;

  private _counterAhorro$ = new Subject();
  private _counterToneladas$ = new Subject();
  private _counterDestinos$ = new Subject();

  private _onDestroyAhorro$ = new Subject();
  private _onDestroyToneladas$ = new Subject();
  private _onDestroyDestinos$ = new Subject();

  private countInterval = 20;
  private numeroDiferencia = 50;

  private datosActualizados: boolean = false;
  private datosConsultados: boolean = false;

  constructor(private _authService: AuthService, private _publicService: PublicService) {

  }

  ngOnInit() {
    this._authService.validaIndex();
    this.getEstadisticasHome();
    this.activarContadores();
  }


  getEstadisticasHome() {
    this._publicService.getHome().subscribe(data => {
      this.ahorroGenerado = data.ahorroGenerado.toFixed();
      this.ahorroGeneradoContador = this.ahorroGenerado - this.numeroDiferencia;
      this.ahorroGeneradoContador = this.ahorroGeneradoContador < 0 ? 0 : this.ahorroGeneradoContador;

      this.toneladasTrasladadas = data.toneladasTrasladadas.toFixed();
      this.toneladasTrasladadasContador = this.toneladasTrasladadas - this.numeroDiferencia;
      this.toneladasTrasladadasContador = this.toneladasTrasladadasContador < 0 ? 0 : this.toneladasTrasladadasContador;


      this.destinos = data.destinos.toFixed();
      this.destinosContador = this.destinos - this.numeroDiferencia;
      this.destinosContador = this.destinosContador < 0 ? 0 : this.destinosContador;

      this.datosConsultados = true;
    });
  }

  actualizarDatosContador(){
    if (!this.datosActualizados && this.datosConsultados){
      this._counterAhorro$.next(this.ahorroGenerado);
      this._counterToneladas$.next(this.toneladasTrasladadas);
      this._counterDestinos$.next(this.destinos);
      this.datosActualizados = true;
    }
  }

  scrollToElement($element): void {
    $element.scrollIntoView({behavior: "smooth", block: "start", inline: "nearest"});
  }

  activarContadores(){
    this._counterAhorro$
      .pipe(
        switchMap(endRange => {
          return timer(0, this.countInterval).pipe(
            mapTo(IndexComponent.positiveOrNegative(endRange, this.ahorroGeneradoContador)),
            startWith(this.ahorroGeneradoContador),
            scan((acc: number, curr: number) => acc + curr),
            takeWhile(this.isApproachingRange(endRange, this.ahorroGeneradoContador))
          )
        }),
        takeUntil(this._onDestroyAhorro$)
      )
      .subscribe((val: number) => this.ahorroGeneradoContador = val);

    this._counterToneladas$
      .pipe(
        switchMap(endRange => {
          return timer(0, this.countInterval).pipe(
            mapTo(IndexComponent.positiveOrNegative(endRange, this.toneladasTrasladadasContador)),
            startWith(this.toneladasTrasladadasContador),
            scan((acc: number, curr: number) => acc + curr),
            takeWhile(this.isApproachingRange(endRange, this.toneladasTrasladadasContador))
          )
        }),
        takeUntil(this._onDestroyToneladas$)
      )
      .subscribe((val: number) => this.toneladasTrasladadasContador = val);

    this._counterDestinos$
      .pipe(
        switchMap(endRange => {
          return timer(0, this.countInterval).pipe(
            mapTo(IndexComponent.positiveOrNegative(endRange, this.destinosContador)),
            startWith(this.destinosContador),
            scan((acc: number, curr: number) => acc + curr),
            takeWhile(this.isApproachingRange(endRange, this.destinosContador))
          )
        }),
        takeUntil(this._onDestroyDestinos$)
      )
      .subscribe((val: number) => this.destinosContador = val);
  }

  private static positiveOrNegative(endRange, currentNumber) {
    return endRange > currentNumber ? 1 : -1;
  }

  private isApproachingRange(endRange, currentNumber) {
    return endRange > currentNumber
      ? val => val <= endRange
      : val => val >= endRange;
  }

  ngOnDestroy() {
    this._onDestroyAhorro$.next();
    this._onDestroyAhorro$.complete();
    this._onDestroyToneladas$.next();
    this._onDestroyToneladas$.complete();
    this._onDestroyDestinos$.next();
    this._onDestroyDestinos$.complete();
  }

}
