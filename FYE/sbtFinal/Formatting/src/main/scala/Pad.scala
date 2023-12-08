object Pad {
	
	def pre(pad:Int,s:String):String = {
		(" " * (pad - s.length)) + s
	}

	def post(pad:Int,s:String):String = {
		s + (" " * (pad - s.length))
	}

}
