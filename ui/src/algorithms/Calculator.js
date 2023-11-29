// src/algorithms/Calculator.js

class Calculator {
  static calcTexi(dist, time) {
    const teximeter = 131;
    let rst = (dist / teximeter) * 100;
    rst += (time / 30) * 100;
    return Math.floor(rst * 1.2) + 4800;
  }
}

export default Calculator;
