

  export const normalizeCurrency = (value,previousValue ) => {
    if (!value) return;
    if (!value) {
      return value
    }
    //value.replace(/[^\d\.]/g, '');  better 
   //const currency = value.replace(/[^.\d]/g, '').replace(/^(\d*\.?)|(\d*)\.?/g, "$1$2");   //original
    const currency = value.replace(/[^\d\.]/g, '')
    return currency ;
  } 

  export const currencyValidation = value =>
  value && !/^[1-9][0-9]{0,12}(\.\d{1,2})?$/.test(value) ?
  'Valor inválido (Ejemplo 124.57)' : undefined;


  export const onlyNums = (value,previousValue ) => {
    if (!value) return;
    if (!value) {
      return value
    }
    //value.replace(/[^\d\.]/g, '');  better 
   //const currency = value.replace(/[^.\d]/g, '').replace(/^(\d*\.?)|(\d*)\.?/g, "$1$2");   //original
    const number = value.replace(/[^\d]/g, '')
    return number ;
  } 