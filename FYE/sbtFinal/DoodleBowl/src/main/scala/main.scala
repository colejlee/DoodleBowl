object Main extends App {

/*
	if (args.length < 1) {
		println("usage: <elements_file>")
		System.exit(0)
	}

	val fileName:String = args(0)
*/

	val game:GameClass = new GameClass("elements.txt")
	game.run()

}
