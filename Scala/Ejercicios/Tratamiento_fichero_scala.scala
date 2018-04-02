import scala.io.Source

object Final_Modulo_Uno {

  case class Campos (fecha: String, producto: String,valor: Int, tipo: String, pais: String)

  class Carga_fichero (val filename: String){
    def readFile(): Seq[Campos] = {
      for {
        line <- Source.fromFile(filename).getLines().drop(1).toVector
        values = line.split(",").map(_.trim)
      } yield Campos(values(0),values(1),values(2).toInt,values(3),values(4))
    }
  }

  class Funciones (val entrada: Any){

    //1. Número total de transacciones en el fichero.
    def f_numero_transacciones (v_list: Seq[Campos]): String ={ val v_string =  v_list.size;  s"1- El número de transacciones es $v_string"}

    //2. Imprimir los datos de la Venta número 100.
    def f_venta_numero (v_list: Seq[Campos], v_venta_numero: Int): String = {val v_string = v_list.slice(v_venta_numero, v_venta_numero + 1);  s"2- La venta número $v_venta_numero es $v_string)"}

    //3. Venta Media por tipo de pago.
    def f_venta_media (val_seq: Seq[Campos]) :Map[String,Double] = {val v_media: Map[String,Double] = val_seq.groupBy(_.tipo).mapValues (_.map(_.tipo).size ) ; v_media}

    //4. Venta Total por tipo de pago.
    def f_venta_total (val_seq: Seq[Campos]) :Map[String,Double]  = {val v_media: Map[String,Double] = val_seq.groupBy(_.tipo).mapValues (_.map(_.valor).sum ) ; v_media}

    //5. Número de ventas totales por día.
    def f_dia_mes (val_seq:  Campos ):  String = {val v_split = val_seq.fecha.split(" ")(0).split("-");  val v_fecha = v_split(2) + "/" + v_split(1); v_fecha}
    def f_venta_total_dia (val_seq: Seq[Campos]): Map[String, Int] = {val dia_mes = val_seq.groupBy(f_dia_mes).mapValues(_.map(_.valor).size ); dia_mes}

    //6. Venta Media por día.
    def f_venta_media_dia (val_seq: Seq[Campos]): Map[String, Int] = {val dia_mes = val_seq.groupBy(f_dia_mes).mapValues(_.map(_.valor).sum ); dia_mes}
    def venta_total_media_dia (val_seq: Seq[Campos]) :Map[String,Int] ={f_venta_media_dia (val_seq).map { case (k, v) => (k, v  / f_venta_total_dia(val_seq).getOrElse(k, 1 )) } }

    //7. Venta Media del día 9/11.
    def venta_media_dia (v_seq: Seq[Campos], v_dia: String): Map[String, Int] = { venta_total_media_dia(v_seq) filter (_._1 == v_dia) }

    //8. Número de Ventas y total de ventas en USA.
    def ventas_pais (v_seq: Seq[Campos], v_pais: String ): List[Int]  = { val v_resultado = List(v_seq.filter( _.pais == v_pais).size, v_seq.filter( _.pais == v_pais).map(_.valor).sum) ; v_resultado  }

    //9. Número de Ventas y total de ventas fuera de USA.
    def ventas_fuera_pais (v_seq: Seq[Campos], v_pais: String ): List[Int]  = { val v_resultado = List(v_seq.filter( _.pais != v_pais).size, v_seq.filter( _.pais != v_pais).map(_.valor).sum) ; v_resultado  }

    //10. Venta Media por día en Francia.
    def ventas_media_pais (v_seq: Seq[Campos], v_pais: String ): Map[String,Int] = {v_seq.filter( _.pais == v_pais).groupBy(f_dia_mes).mapValues(_.map(_.valor).sum) }

    //11. Día Máxima Venta Media fuera de Rusia.
    def sum_venta_norusia (val_seq: Seq[Campos],v_pais: String): Map[String, Int]= {val v_sum_venta_norusia = val_seq.filter( _.pais != v_pais).groupBy(f_dia_mes).mapValues(_.map(_.valor).sum); v_sum_venta_norusia}
    def total_venta_norusia (val_seq: Seq[Campos],v_pais: String): Map[String, Int] = {val v_total_venta_norusia = val_seq.filter( _.pais != v_pais).groupBy(f_dia_mes).mapValues(_.map(_.valor).size); v_total_venta_norusia }

    //12. Venta por tipo de pago en Italia.
    def venta_tipo_pago_pais (v_seq: Seq[Campos], v_pais: String ): Map[String,Int] = {val v_venta_tipo = v_seq.filter( _.pais == v_pais).groupBy(_.tipo).mapValues(_.map(_.valor).sum); v_venta_tipo}

    //13. Venta de Visa en Italia.
    def venta_tipo_pago (v_seq: Seq[Campos], v_pais: String, v_tipo_pago: String ): Any =  { val valor: Map[String,Int] =  v_seq.filter( _.pais == v_pais).groupBy(_.tipo).mapValues(_.map(_.valor).sum).filterKeys(_ == v_tipo_pago); valor.values}
  }

  def main(args: Array[String]): Unit = {

    //val filename = "C:\\Users\\a451434\\IdeaProjects\\Pruebas_Idea\\src\\Transacciones.txt"
    val filename  = "/Users/ivaniglesiasalcon/Documents/Scala_Projects/project/Transacciones.txt"

    val v_carga_fichero = new Carga_fichero(filename)

    val v_llamadas = new Funciones

    val campos = v_carga_fichero.readFile()

    //1. Número total de transacciones en el fichero.
    println(v_llamadas.f_numero_transacciones(campos))

    //2. Imprimir los datos de la Venta número 100.
    println(v_llamadas.f_venta_numero(campos, 100))

    //3. Venta Media por tipo de pago.
    println( "3- Venta Media por tipo de pago "+ v_llamadas.f_venta_total(campos).map { case (k, v) => (k, (v / v_llamadas.f_venta_media(campos).getOrElse(k, 1.0))round) })

    //4. Venta Total por tipo de pago.
    println("4- Venta Total por tipo de pago " + v_llamadas.f_venta_total(campos))

    //5. Número de ventas totales por día.
    println("5- Número de ventas totales por día "+ v_llamadas.f_venta_total_dia(campos))

    //6. Venta Media por día.
    println("6- Venta media por día " + v_llamadas.venta_total_media_dia(campos))

    //7. Venta Media del día 9/11.
    println( "7- Venta media día 9/11 " + v_llamadas.venta_media_dia (campos, "9/11"))

    //8. Número de Ventas y total de ventas en USA.
    println ("8- Ventas en USA " +  v_llamadas.ventas_pais(campos, "USA") )

    //9. Número de Ventas y total de ventas fuera de USA.
    println ("9- Ventas fuera USA " +  v_llamadas.ventas_fuera_pais(campos, "USA") )

    //10. Venta Media por día en Francia.
    println("10- Ventas Media por día Francia " + v_llamadas.ventas_media_pais (campos, "Francia"))

    //11. Día Máxima Venta Media fuera de Rusia.
    println("11- Día máxima Venta Media fuera Rusia " + v_llamadas.sum_venta_norusia(campos, "Rusia").map { case (k, v) => (k, v  / v_llamadas.total_venta_norusia(campos, "Rusia").getOrElse(k, 1 )) }.maxBy(_._2) )

    //12. Venta por tipo de pago en Italia.
    println("12- Ventas por tipo de pago en Italia " + v_llamadas.venta_tipo_pago_pais ( campos, "Italia"))

    //13. Venta de Visa en Italia.
    println("13- Ventas de Visa en Italia " + v_llamadas.venta_tipo_pago (campos,"Italia", "Visa"))

  }

}


