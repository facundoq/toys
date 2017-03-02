package mazes.webapp

import mazes.generators.DungeonGenerator.Map

abstract class DungeonUI{
  def dungeonToText(d: Map): String = {
    var r = ""

    for (i <- 0 until d.rows) {
      for (j <- 0 until d.columns) {
        r += d(i, j).asciiArt + ""
      }
      r += "\n"
    }
    return r

  }
}