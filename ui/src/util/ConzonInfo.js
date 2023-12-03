// ConzonInfoReact.js

class ConzonInfoReact {
  static conzonDict = new Map();
  static adjacent = [];
  static conzonID = new Map();
  static lineInfo = new Map();

  static initialize() {
    try {
      const fs = require("fs");
      const csv = require("csv-parser");

      // Load data and initialize
      const conzonData = fs.readFileSync("assets/conzon_info.csv", "utf8");
      const lineData = fs.readFileSync("assets/line_info.csv", "utf8");

      this.parseConzon(conzonData);
      this.parseLine(lineData);
    } catch (error) {
      throw new Error(error);
    }
  }

  static parseConzon(str) {
    const lines = str.split("\n");
    lines.shift(); // Remove header

    lines.forEach((line) => {
      const elements = line.split(",");

      try {
        const idx = parseInt(elements[3]);
        const par = elements[9];
        const split = par.split("-");

        if (!this.conzonDict.has(idx)) this.conzonDict.set(idx, split[0]);

        const from = parseInt(elements[3]);
        const to = parseInt(elements[4]);
        const dist = parseInt(parseFloat(elements[1]));
        const lineNum = parseInt(parseFloat(elements[6]));
        const lanecnt = parseInt(parseFloat(elements[5]));

        this.adjacent[from].push(new ConzonNode(to, dist, lineNum, lanecnt));
        this.conzonID.set(elements[0], [from, to]);
      } catch (ignored) {}
    });
  }

  static parseLine(str) {
    const lines = str.split("\n");
    lines.shift(); // Remove header

    lines.forEach((line) => {
      const elements = line.split(",");

      try {
        const lineNum = parseInt(elements[0]);
        this.lineInfo.set(lineNum, elements[1]);
      } catch (ignored) {}
    });
  }

  static getLineInfo() {
    return this.lineInfo;
  }

  static getConzonDict() {
    return this.conzonDict;
  }

  static getAdjacent() {
    return this.adjacent;
  }

  static getConzonID() {
    return this.conzonID;
  }
}

export default ConzonInfoReact;
