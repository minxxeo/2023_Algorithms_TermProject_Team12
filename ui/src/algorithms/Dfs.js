import ConzonInfo from "../util/ConzonInfo";

class PriorityQueue {
  constructor() {
    this.queue = [];
  }

  offer(node) {
    this.queue.push(node);
    this.queue.sort((a, b) => a.compareTo(b));
  }

  poll() {
    return this.queue.shift();
  }

  isEmpty() {
    return this.queue.length === 0;
  }
}

class Dfs {
  constructor() {
    this.dist = new Array(1100).fill(2147483647);
    this.prevLoc = new Array(1100).fill(0);
    this.time = new Array(1100).fill(0);
    // External dependencies like ConzonInfo should be appropriately defined here
    this.adjacent = ConzonInfo.getAdjacent();
    this.conzonDict = ConzonInfo.getConzonDict();
    this.lineInfo = ConzonInfo.getLineInfo();
  }

  dijkstra(from, mode) {
    const dist = new Array(1100).fill(2147483647);
    const prevLoc = new Array(1100).fill(0);
    const pq = new PriorityQueue();

    dist[from] = 0;
    pq.offer(new Node(from, 0));

    while (!pq.isEmpty()) {
      const cur = pq.poll().idx;

      for (const curNode of this.adjacent[cur]) {
        const newDist =
          dist[cur] + (mode === 1 ? curNode.getDist() : curNode.getTime());

        if (dist[curNode.getId()] > newDist) {
          dist[curNode.getId()] = newDist;
          prevLoc[curNode.getId()] = cur;
          pq.offer(new Node(curNode.getId(), newDist));
        }
      }
    }

    this.dist = dist;
    this.prevLoc = prevLoc;
  }

  getStart2End(from, to, mode) {
    this.dijkstra(from, mode);
    let cur = to;
    const line = [];

    while (true) {
      line.push(cur);
      if (cur === this.from) break;
      cur = this.prevLoc[cur];
    }

    return { distance: this.dist[to], route: line.reverse() };
  }

  printRoute(line) {
    for (let i = 0; i < line.length; i++) {
      if (
        this.conzonDict[line[i]].includes("IC") &&
        !(i === 0 || i === line.length - 1)
      )
        continue;
      console.log(
        this.conzonDict[line[i]] + (i !== line.length - 1 ? " - " : "")
      );
    }
  }

  printLine(line) {
    let cur = -1;
    const rst = [];

    for (let i = line.length - 2; i >= 0; i--) {
      for (const iter of this.adjacent[line[i]]) {
        if (iter.getId() === line[i + 1] && iter.getLine() !== cur) {
          cur = iter.getLine();
          rst.push(this.lineInfo[cur]);
        }
      }
    }

    for (let i = 0; i < rst.length; i++) {
      console.log(rst[i] + (i !== rst.length - 1 ? " - " : ""));
    }
  }

  calcToll(line) {
    let cost = 44.3;
    let rst = 0;

    for (let i = line.length - 2; i >= 0; i--) {
      for (const iter of this.adjacent[line[i]]) {
        if (iter.getId() === line[i + 1]) {
          if (iter.getLanecnt() >= 6) {
            rst += (iter.getDist() / 1000) * cost * 1.2;
          } else {
            rst += (iter.getDist() / 1000) * cost;
          }
        }
      }
    }

    rst += 1800;
    return Math.floor(rst);
  }
}

class Node {
  constructor(idx, cost) {
    this.idx = idx;
    this.cost = cost;
  }

  compareTo(o) {
    return this.cost - o.cost;
  }
}

export default Dfs;
