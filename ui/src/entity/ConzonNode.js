// src/entity/ConzonNode.js

class ConzonNode {
  constructor(id, dist, line, lanecnt) {
    this.id = id;
    this.dist = dist;
    this.line = line;
    this.lanecnt = lanecnt;
    this.time = 0;
  }

  setTime(time) {
    this.time = time;
  }

  getTime() {
    return this.time;
  }

  getLanecnt() {
    return this.lanecnt;
  }

  getId() {
    return this.id;
  }

  getDist() {
    return this.dist;
  }

  getLine() {
    return this.line;
  }
}

export default ConzonNode;
