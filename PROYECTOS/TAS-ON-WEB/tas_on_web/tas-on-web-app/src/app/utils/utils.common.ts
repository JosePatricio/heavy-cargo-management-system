import {Headers} from "@angular/http";
import {appConfig} from "../app.config";
import {DatePipe} from "@angular/common";

export class UtilsCommon {

  public getPublicHeader() {
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');
    headers.append('Authorization', 'Basic ' + btoa(appConfig.PUBLIC_USER + ':' + appConfig.PUBLIC_PASSWORD));
    return {headers: headers}
  }

  public static numericFormat(valor: any) {
    let NumericValue = parseFloat(valor);
    return NumericValue.toFixed(2);
  }

  public static numericFormatDigit(valor: any, decimals: any) {
    let NumericValue = parseFloat(valor);
    return NumericValue.toFixed(decimals);
  }

  public static filterData(data: any, valueFilter: any, key: any) {
    let value_: any = {};
    data.forEach(data_ => {
      Object.keys(data_).forEach((value: any, key_: any) => {
        if (value.localeCompare(key) == 0)
          if (Object.values(data_)[key_].localeCompare(valueFilter) == 0)
            value_ = data_;
      });
    });
    return value_;
  }

  public static dataForDatePiker(date: any) {
    const pipe = new DatePipe('es-EC');
    let nowDate = new Date();
    let date_ = {
      date: {
        year: parseInt(pipe.transform(nowDate, 'yyyy')),
        month: parseInt(pipe.transform(nowDate, 'MM')),
        day: parseInt(pipe.transform(nowDate, 'dd'))
      },
      formatted: pipe.transform(nowDate, appConfig.formatoFechaHoraSegundos),
      formattedURL: pipe.transform(nowDate, appConfig.formatoFechaHoraURL),
      jsdate: nowDate
    };
    try {
      let y = parseInt(pipe.transform(date, 'yyyy'));
      let m = parseInt(pipe.transform(date, 'MM'));
      let d = parseInt(pipe.transform(date, 'dd'));

      date_ = {
        date: {year: y, month: m, day: d},
        formatted: pipe.transform(date, appConfig.formatoFechaHoraSegundos),
        formattedURL: pipe.transform(date, appConfig.formatoFechaHoraURL),
        jsdate: new Date(y + '-' + m + '-' + d)
      };
    } catch (e) {
      console.log(e);
    }
    return date_;
  }

  public static image64Value(image64) {
    let imageRecive: any;
    try {
      imageRecive = 'data:image/jpg;base64,' + image64;
    } catch (e) {
      console.log(e);
    }
    return imageRecive;
  }

  public static image64Val(imgElem, image64) {
    var imgDefault = '../../../assets/images/logo.png';
    if (this.empty(image64))
      document.getElementById(imgElem).setAttribute('src', imgDefault);
    else
      document.getElementById(imgElem).setAttribute('src', "data:image/jpg;base64," + image64);
  }

  public static empty(param) {
    if (param === 'undefined' || !param || param === ""
      || param === null || param.length === 0)
      return true;
    else
      return false;
  }

  public static formatNumeric(ofertaValor: any, comision: any) {
    var valor = 0;
    if (comision)
      valor = (ofertaValor + comision);
    else
      valor = ofertaValor;
    return this.numericFormat(valor);
  }

  public static getOneComparators(compareArray, validation: any) {
    return (compareArray.indexOf(validation) > -1);
  }

  public static restrictedUser(userType: number) {
    var restrictedTypes = [82];
    return this.getOneComparators(restrictedTypes, userType);
  }
}
