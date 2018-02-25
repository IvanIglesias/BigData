import requests
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from pandas import ExcelWriter
import os
v_os = os.getcwd()


def fMenorValor(a, vSeriesProducto):
    'Funcion que devuelve el mes de menor valor de la Serie Producto'

    for i, x in vSeriesProducto.items():
        if x == a:
            return(i)

def fMayorValor(a, vSeriesProducto):
    'Funcion que devuelve el mes de mayor valor de la Serie Producto'


    for i, x in vSeriesProducto.items():
        if x == a:
            return(i)

def datos():
    #Creación lista meses
    arMeses = ['Enero', 'Febrero', 'Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre']
    #Creación DataFrame datatos Productos del año elegido
    datPrecios = pd.read_excel (v_os + '/PRECIOS_2002_2017.xls', skiprows=4, index_col=  0, names =arMeses ,  sheetname = '2016')

    #Creación de excel con los datos de limpios del año alegido
    #writer = ExcelWriter('./datos/output.xlsx') #Es la ruta antigua
    writer = ExcelWriter(v_os + '/output.xlsx') ##Es la ruta antigua
    datPrecios.to_excel(writer,'2016')

    writer.save()

    lista = list(datPrecios.iloc[:,1])
    listNumAgrupacion=[i for i,x in enumerate(lista) if x=='F']
    listNombreAgrupacion = list(datPrecios[datPrecios.iloc[:,1].replace(' ','_') == 'F'].index)
    listaAgrupacion = list(zip(listNumAgrupacion,listNombreAgrupacion))

    serie = pd.Series(listaAgrupacion)


    def fEvolucion (vAlimento):
        'Función que crea una lista de Todos los productos'
        for p,l in serie.iteritems():
            if vAlimento in l:
                vInicio,a = l
                if p+1 < len(serie):
                    vFinal,b = serie[p+1]
                else:
                    vFinal = len(datPrecios)
                return(vInicio,vFinal)
    #Se recupera Frutas frescas como ejemplo y por ser productos con temporada
    vInicio,vFinal = fEvolucion('FRUTAS FRESCAS')
    #listaAgrupacion contiene todas las agrupaciones de productos
    #print(listaAgrupacion)

    #Creación de DataFrame indicando la primera y última posición de
    #frutas frescas dentro del DataFrame DatPrecios
    datProductos = (datPrecios[vInicio+1:vFinal])
    #ejemplo de visualización de dos frutas
    print(datProductos[5:7].T.plot(grid = True,title = 'Precio/Mes', ﬁgsize=(15,7)) )
    #DataFrame con todos los productos del mes seleccionado sin los registros
    #  de la agrupación
    datPrecios = datPrecios[datPrecios.iloc[:,1] != 'F']
    #Ejemplo de visualización de varios productos
    ax1,ax2,ax3, ﬁg = datPrecios.iloc[5:10,1:5].plot( kind = 'barh' , subplots = True, title = 'Precio/Mes', ﬁgsize=(10,10))

    #Generación de excel con los datos de Frutas Frescas
    datExcel = datProductos
    writer = ExcelWriter(v_os + '/Frutas.xlsx')
    datExcel.to_excel(writer,'Hoja 1')

    writer.save()

    #Generación de excel con los datos de Frutas Frescas T
    datExcelT = datProductos.T
    writer = ExcelWriter(v_os + '/FrutasT.xlsx')
    datExcelT.to_excel(writer,'Hoja 1')

    writer.save()

    #DataFrame Frutas Frescas por Mes
    datTemporada = pd.read_excel (v_os + '/Temporada_Frutas.xlsx', index_col=  0, names =arMeses )

    mes = input ("Escribe Mes : ")
    #Se crea el objeto que almacenará los productos de temporada del mes
    #elegido
    if mes in  arMeses:
        datFrutaTemporada = datTemporada[mes].dropna()
    else:
        mes = 'Enero'#si no se elige més se recupera por defecto Enero
        datFrutaTemporada = datTemporada['Enero'].dropna()

    print(mes)

    print(datFrutaTemporada)

    #Array de Frutas, para imprimir la selección de la fruta
    arFrut = []
    for x in datFrutaTemporada.index:
        arFrut.append (x)

    datProductos.loc[arFrut[0:len(arFrut)],mes]

    datProductos.loc[arFrut[0:len(arFrut)]]


    daProductosTemporada = datProductos.loc[[x for x in datFrutaTemporada.index]]
    daProductosTemporada = daProductosTemporada.dropna(axis = 0 , thresh = 3)
    daProductosTemporada.plot.barh(title = 'Precios anuales productos temporada ' + mes ,stacked=True, alpha=0.7,figsize=(10,5));


    datProPreciosTemp = daProductosTemporada#daProductosTemporada.loc[arFrut[0:len(arFrut)]].dropna()

    #datProPreciosTemp.index
    list(datProPreciosTemp.index)


    producto = input ("Escribe Producto de la lista anterior: ")

    #Tratamiento producto elegido
    if producto in datProPreciosTemp.index:
        vSeriesProducto = pd.Series(datProPreciosTemp.loc[producto].T)
    else:
        producto = 'Piñas'
        vSeriesProducto = pd.Series(datProPreciosTemp.loc[producto].T)
    #vSeriesProducto.dropna().plot(title = producto, figsize = (12,3) );
    vSeriesProducto = vSeriesProducto.dropna()


    #precios Año producto elegido
    vSeriesProducto


    #Visualización precios año del producto elegido
    vSeriesProducto.plot.barh(title = 'Precios ' +producto  ,stacked=True, alpha=0.5);



    #VISUALIZACIÓN MEJOR Y PEOR MES PRODUCTO
    vMenorIndex = vSeriesProducto.values.min()
    vMayorIndex = vSeriesProducto.values.max()
    vMenorValor = fMenorValor(vMenorIndex, vSeriesProducto)
    vMayorValor = fMayorValor(vMayorIndex, vSeriesProducto)
    x = vSeriesProducto[vMenorValor]
    y = vSeriesProducto[vMayorValor]

    fig, ax = plt.subplots()
    lines = ax.plot(x,y , label = 'Precio')
    ax.title.set_text(producto)
    ax.grid(True)

    ax.axes.xaxis.set_label_text(vMenorValor, fontdict={"size":10})
    ax.axes.yaxis.set_label_text(vMayorValor, fontdict={"size":10})
    ax.axis('tight');
    ax.scatter(x,y);

    #Informe de Valores del Producto
    print('Mes más caro: ',vMayorValor), print('Mes más Barato: ',vMenorValor)

    print('Precio Máximo ', vMayorIndex), print('Precio Mínimo ', vMenorIndex)


    print('Precio Medio ', vSeriesProducto.median()), print('Precio Mediana ', vSeriesProducto.mean())


def main():


    #%matplotlib inline
    datos()


if __name__ == "__main__":

    main()
