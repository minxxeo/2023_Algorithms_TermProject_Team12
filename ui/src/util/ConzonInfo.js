// src/util/conzonInfo.js
import ConzonNode from "../entity/ConzonNode";

const fs = require("fs");
const { promisify } = require("util");

const readFileAsync = promisify(fs.readFile);

let conzonDict;
let adjacent;
let conzonID;
let lineInfo;

async function initialize() {
  try {
    const conzonFile = await readFileAsync(
      "../assets/conzon_info.csv",
      "EUC-KR"
    );
    const lineFile = await readFileAsync("../assets/line_info.csv", "EUC-KR");

    conzonDict = new Map();
    adjacent = Array.from({ length: 1100 }, () => []);

    conzonID = new Map();
    lineInfo = new Map();

    const conzonLines = conzonFile.split("\n").slice(1);
    conzonLines.forEach(parse_conzon);

    const lineLines = lineFile.split("\n").slice(1);
    lineLines.forEach(parse_line);
  } catch (error) {
    throw new Error(error);
  }
}

async function initializeWithTime(t) {
  try {
    const conzonFile = await readFileAsync("conzon_info.csv", "utf-8");

    adjacent = Array.from({ length: 1100 }, () => []);

    const conzonLines = conzonFile.split("\n").slice(1);
    conzonLines.forEach((str) => parse_conzon_t(str, t));
  } catch (error) {
    throw new Error(error);
  }
}

function parse_conzon(str) {
  const element = str.split(",");
  try {
    const idx = parseInt(element[3]);
    const par = element[9];
    const split = par.split("-");

    if (!conzonDict.has(idx)) conzonDict.set(idx, split[0]);

    const from = parseInt(element[3]);
    const to = parseInt(element[4]);
    const dist = parseInt(parseFloat(element[1]));
    const line = parseInt(parseFloat(element[6]));
    const lanecnt = parseInt(parseFloat(element[5]));

    adjacent[from].push(new ConzonNode(to, dist, line, lanecnt));

    conzonID.set(element[0], [from, to]);
  } catch (ignored) {}
}

function parse_conzon_t(str, t) {
  const element = str.split(",");
  try {
    const idx = parseInt(element[3]);
    const par = element[9];
    const split = par.split("-");

    if (!conzonDict.has(idx)) conzonDict.set(idx, split[0]);

    const from = parseInt(element[3]);
    const to = parseInt(element[4]);
    const dist = parseInt(parseFloat(element[1]));
    let time = 0;

    if (t.has(par)) {
      time = t.get(par);
    } else {
      time = parseInt(((dist / (parseFloat(element[7]) - 20)) * 3600) / 1000);
    }

    for (const iter of adjacent[from]) {
      if (iter.getId() === to) {
        iter.setTime(time);
        break;
      }
    }
  } catch (ignored) {}
}

function parse_line(str) {
  const element = str.split(",");
  try {
    const line = parseInt(element[0]);
    lineInfo.set(line, element[1]);
  } catch (ignored) {}
}

function getLineInfo() {
  return lineInfo;
}

function getConzonDict() {
  return conzonDict;
}

function getAdjacent() {
  return adjacent;
}

function getConzonID() {
  return conzonID;
}

module.exports = {
  initialize,
  initializeWithTime,
  getLineInfo,
  getConzonDict,
  getAdjacent,
  getConzonID,
};
