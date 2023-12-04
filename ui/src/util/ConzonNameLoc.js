// src/entity/ConzonNameLoc.js

class ConzonNameLoc {
  constructor(name, x, y) {
    this.name = name;
    this.x = x;
    this.y = y;
  }

  getName() {
    return this.name;
  }

  setName(name) {
    this.name = name;
  }

  getX() {
    return this.x;
  }

  setX(x) {
    this.x = x;
  }

  getY() {
    return this.y;
  }

  setY(y) {
    this.y = y;
  }
}

module.exports = ConzonNameLoc;
