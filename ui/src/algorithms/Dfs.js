// src/algorithms/Dfs.js
import ConzonInfo from "../util/ConzonInfo";

class Dfs {
  constructor() {
    this.dist = new Array(1100).fill(2147483647);
    this.prev_loc = new Array(1100).fill(0);
    this.time = new Array(1100).fill(2147483647);
    this.adjacent = ConzonInfo.getAdjacent();
    this.conzonDict = ConzonInfo.getConzonDict();
    this.lineInfo = ConzonInfo.getLineInfo();
  }

  getStart2End(from, to, mode) {
    this.dijkstra(from, mode);
    let cur = to;
    const line = [];
    while (true) {
      line.push(cur);
      if (cur === from) break;
      cur = this.prev_loc[cur];
    }
    return [this.dist[to], line.reverse()];
  }

  dijkstra(from, mode) {
    this.dist.fill(2147483647);
    this.prev_loc.fill(0);
    this.time.fill(2147483647);

    const pq = new PriorityQueue();
    let newdist;

    this.dist[from] = 0;
    pq.offer(new Node(from, 0));

    while (!pq.isEmpty()) {
      const cur = pq.poll().idx;
      for (const curNode of this.adjacent[cur]) {
        newdist = this.dist[cur] + (mode === 1 ? curNode.dist : curNode.time);
        if (this.dist[curNode.id] > newdist) {
          this.dist[curNode.id] = newdist;
          this.prev_loc[curNode.id] = cur;
          pq.offer(new Node(curNode.id, newdist));
        }
      }
    }
  }

  printRoute(line) {
    for (let i = line.length - 1; i >= 0; i--) {
      if (
        this.conzonDict.get(line[i]).includes("IC") &&
        !(i === 0 || i === line.length - 1)
      )
        continue;
      process.stdout.write(
        this.conzonDict.get(line[i]) + (i !== 0 ? " - " : "\n")
      );
    }
  }

  printLine(line) {
    let cur = -1;
    const rst = [];

    for (let i = line.length - 2; i >= 0; i--) {
      for (const iter of this.adjacent[line[i]]) {
        if (iter.id === line[i + 1] && iter.line !== cur) {
          cur = iter.line;
          rst.push(this.lineInfo.get(cur));
        }
      }
    }
    for (let i = 0; i < rst.length; i++) {
      process.stdout.write(rst[i] + (i !== rst.length - 1 ? " - " : "\n"));
    }
  }

  calcToll(line) {
    const cost = 44.3;
    let rst = 0;

    for (let i = line.length - 2; i >= 0; i--) {
      for (const iter of this.adjacent[line[i]]) {
        if (iter.id === line[i + 1]) {
          if (iter.lanecnt >= 6) {
            rst += (iter.dist / 1000) * cost * 1.2;
          } else {
            rst += (iter.dist / 1000) * cost;
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

// Export the Dfs class
module.exports = Dfs;

class PriorityQueue {
  constructor() {
    this.heap = [];
  }

  isEmpty() {
    return this.heap.length === 0;
  }

  offer(node) {
    this.heap.push(node);
    this.heap.sort((a, b) => a.compareTo(b));
  }

  poll() {
    if (this.isEmpty()) return null;
    return this.heap.shift();
  }
}
