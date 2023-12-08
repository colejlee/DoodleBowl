import scala.io.Source
import scala.collection.mutable.Map
import scala.io.StdIn._


object Main extends App {
	val fileName:String = args(0)
	val game:GameClass = new GameClass(fileName)

	game.run()
}

class GameClass(fileName:String) {
	private val numElems:Int = Source.fromFile(fileName).getLines().toList.length
	private val elements:Map[String,Int] = Map[String,Int]()
	private val names:Array[String] = Array.tabulate(numElems)(i => "temp" + i)
	private val matrix:Array[Array[Int]] = Array.tabulate(numElems)(_ => Array.tabulate(numElems)(_ => -1))
	private val existingElems:Array[Int]  = Array.tabulate(numElems)(_ => 0)

	var idx:Int = 0
	for (line <- Source.fromFile(fileName).getLines()) {
		val elems:Array[String] = line.split(",")
		elements += (elems(0).toLowerCase() -> idx)
		names(idx) = elems(0)
		if (elems.length == 3) {
			val base1:Int = elements(elems(1).toLowerCase())
			val base2:Int = elements(elems(2).toLowerCase())
			matrix(base1)(base2) = idx
			matrix(base2)(base1) = idx
		} else {
			existingElems(idx) = 1
		}
		idx += 1
	}

	private var running:Boolean = false
	private var exitString:String = "Game Over"

	def run():Unit = {
		println("Hello! This is a simulation of Earth at the beginning of civilization.")
		println("Do you have what it takes to bring this world out of the dark ages?\n")
		println("Directions: Combine different elements to create new ones!")
		println("Input each element when prompted. Create all the elements to beat the game.")
		println("If you do not wish to continue playing, type 'no' when prompted.")
		running = true
		while (running) {
			print("Would you like to continue? ")
			if (readLine().toLowerCase() != "no") {
				creationLoop()
			} else {
				running = false
				exitString = "Thanks for playing! Goodbye."
			}
		}
		println(exitString)
	}

	private def creationLoop():Unit = {
		printExistingElems("\nCombine two elements:\n")
		val element1:Int = inputElement("\nElement 1: ")
		val element2:Int = inputElement("Element 2: ")
		matrix(element1)(element2) match {
			case -1 => println("Not a valid reaction. Please try again.")
			case x =>
				existingElems(x) match {
					case 0 						=> 
						var name:String = names(x)
						//if (name == "God")
							//name += "?"
						name match {
							case "God"	=> name += "?"
							case _		=> 
						}
						println(s"You have created $name")
					case e if e > 0 && e < 3 	=> println(names(x) + " already exists")
					case e if e >= 3 && e < 5 	=> println("We get it, " + names(element1) + " and " + names(element2) + " make " + names(x) + ".")
					case _ 						=> println("For fucks sake, you have already created " + names(x) + "!")
				}
				existingElems(x) += 1
		}

		if (numElems == existingElems.filter(x => x > 0).length) {
			exitString = "Congratulations! You have created all of the elements!"
			running = false
		}
	}

	private def inputElement(prompt:String):Int = {
		print(prompt)
		elements.getOrElse(readLine().toLowerCase(), -1) match {
			case -1 =>
				println("Sorry, that is not an existing element. Please enter an existing element.")
				inputElement(prompt)
			case elem => elem
		}
	}
	
	private def printExistingElems(header:String = "Existing Elements"):Unit  = {
		val numColumns:Int = 3
		val pad:Int = names.map(s => s.length).max +3
		var numPrint:Int = 0
		
		println(header)

		for (i <- 0 until numElems) {
			if (existingElems(i) > 0) {
				print(Pad.post(pad,names(i)))
				numPrint += 1
				if (numPrint % numColumns == 0) {
					println("\n")
				}
			}
		}
		if (numPrint % numColumns != 0) {
			println()
		}
	}

	def printMatrix():Unit ={
		val pad:Int = names.map(s => s.length).max + 1
		print(Pad.pre(pad + 1, ""))
		for (name <- names) 
			print(Pad.pre(pad,name))
		println()
		for (i <- 0 until numElems) {
			print(Pad.post(pad,names(i)) + "[")
			for (e <- matrix(i)) {
				print(Pad.pre(pad, e match {
					case -1 => "--"
					case _ => names(e)
				}))
			}
			println("]")
		}
	}	

}


object Pad {
	
	def pre(pad:Int,s:String):String = {
		(" " * (pad - s.length)) + s
	}

	def post(pad:Int,s:String):String = {
		s + (" " * (pad - s.length))
	}

}
