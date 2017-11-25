 
'---------------------------------------------------------------------------------------
' Projet     : SCG
' Module     : oSCG  [Module de classe]
' Version    : 1.03
' Création   : Le jeudi 26 juin 2008
' Auteur     : P.B. [Philben] - <a href="http://www.developpez.net" target="_blank">http://www.developpez.net</a> (forum : Access -> Contribuez)
' Objet      : Conversion de coordonnées planes ou géographiques de systèmes géodésiques
' Références : <a href="http://www.IGN.fr" target="_blank">http://www.IGN.fr</a> (Institut Géographique National - France)
'              <a href="http://www.NGI.be" target="_blank">http://www.NGI.be</a> (Institut Géographique National - Belgique)
' Exemple    : - Conversion de Lambert II étendu (X et Y en mètres) vers WGS84 (Lon, Lat)
'              Dim oConv as oSCG
'              Set oConv = New oSCG
'              oConv.TypeConversion NTF_Lambert_II_Etendu, WGS84_Geo
'              If Not oConv.IsErreur Then
'                 oConv.CalculSCG 647462, 2468321
'                 Debug.Print "Longitude : " & oConv.RadianToDegreDec(oConv.X)
'                 Debug.Print "Latitude  : " & oConv.RadianToDegreDec(oConv.Y)
'              endif
'              Set oConv = Nothing
' Tests      : - 3 fonctions permettent de vérifier les algorithmes, les conversions
'                entres systèmes et les conversions entres les unités d'angle :
'              Dim oConv as oSCG
'              Set oConv = New oSCG
'              oConv.TestAlgos
'              oConv.TestAngles
'              oConv.TestConversions
'              Set oConv = Nothing
'              -> Les Résultats sont affichés dans la fenêtre d'exécution de VBE (Debug.Print)
' Remarque   : Seules certaines conversions sont vérifiées et sur un point seulement
'              => cette classe ne doit être utilisée que dans un but ludo-éducatif
' v1.01      : - Désactivation des conversions pour la belgique car bug persistant...
'              - Ajout test alg54 (bug corrigé)
'              - Ajout de tests de conversion
' v1.02      : - Correction d'un bug dans AngleDMSToString pour les coordonnées négatives
' v1.03      : - Réactivation des conversions pour la Belgique (bugs corrigés)
'              - Modification de quelques noms de fonction, enum, etc.
'              - Correction de quelques bugs mineurs
'              - Ajout fonction DistanceKm qui calcule la distance en km entre 2 Lon/Lat
'---------------------------------------------------------------------------------------
Option Compare Database
Option Explicit
'A vrai pour tester des conversions et/ou les algorithmes (TestAlgos)
#Const CCTEST = True
#If CCTEST Then
Private Enum eUnites
   gr
   rad
   dms
   m
   DD
End Enum
Private Type tResults
   dX As Double
   dY As Double
   sX As String
   sY As String
End Type
#End If
'Enumération des projections et systèmes à convertir
Public Enum eSCG
   BD72_Lambert72         'Projection Lambert 72 - Ellipsoïde Hayford 1924 - Belgique
   BD72_Geo
   ETRS89_Lambert08       'Projection Lambert 2008 - Ellipsoïde GRS80 - Belgique
   ETRS89_Geo             'SG ETRS89 - Ellipsoïde GRS80 - Belgique
   ED50_Geo               'SG European Datum 1950 - Ellipsoïde Hayford 1909
   NTF_Lambert_I          'Projection Lambert I - Ellipsoïde NTF : Clarke 1880 - France
   NTF_Lambert_II         'Projection Lambert II - Ellipsoïde NTF : Clarke 1880 - France
   NTF_Lambert_III        'Projection Lambert III - Ellipsoïde NTF : Clarke 1880 - France
   NTF_Lambert_IV         'Projection Lambert IV - Ellipsoïde NTF : Clarke 1880 - France
   NTF_Lambert_II_Etendu  'Projection Lambert pour la France
   NTF_Paris_Geo          'SG NTF : Nouvelle triangulation de la France (méridien Paris)
   RGF93_Geo              'SG Réseau géodésique français - Ellipsoïde RGF : IAG GRS 80
   RGF93_Lambert_93       'Projection Lambert 93 - RGF : Réseau géodésique Français 1993
   WGS84_Geo              'SG World Geodetic System 1984
End Enum
'Transformations définies dans le programme
Private Enum eTypeTrans
   NTFtoRGF93
   NTFtoED50
   NTFtoWGS84
   RGF93toNTF
   RGF93toED50
   RGF93toWGS84
   ED50toNTF
   ED50toRGF93
   ED50toWGS84
   WGS84toNTF
   WGS84toRGF93
   WGS84toED50
   ETRS89toBD72
   BD72toETRS89
End Enum
'colonnes du tableau des conversions autorisées
'Abréviations :
'  - PLA = Coordonnées planes d'une projection (X et Y en mètre) - Lambert seulement ici
'  - GEO = Coordonnées géographiques d'un système géodésique (lambda et phi en radian)
'  - CAR = Coordonnées cartésiennes d'un système géodésique
'  - lambda = longitude en radian
'  - phi = latitude en radian
'Schémas de conversion :
'  1) Passage de coordonnées cartographiques planes d'une projection d'un système
'     géodésique vers les coordonnées d'une projection d'un autre système géodésique
'     - PLA_1 -> GEO_1 -> CAR_1 -> CAR_2 -> GEO_2 -> PLA_2
'     - Entrée/Sortie (E/S) : X_1 et Y_1 -> X_2 et Y_2 (en mètres)
'     - Exemple : Lambert_I (NTF) vers Lambert93 (RGF93)
'  2) Passage de coordonnées cartographiques planes d'une projection d'un système
'     géodésique vers les coordonnées géographiques d'un autre système géodésique
'     - PLA_1 -> GEO_1 -> CAR_1 -> CAR_2 -> GEO_2
'     - E/S :  X_1 et Y_1 (en mètres) -> lambda_2 et phi_2 (angles en radians avec phi = latitude et lambda = longitude)
'     - Exemple : Lambert_I vers WGS84 (système utilisé par les GPS)
'  3) Passage de coordonnées cartographiques planes d'une projection vers coordonnées
'     cartographiques planes d'une autre projection d'un même système géodésique
'     - PLA_1 -> GEO -> PLA_2
'     - E/S : X_1 et Y_1 -> X_2 et Y_2 (en mètres)
'     - Exemple : Lambert_I vers Lambert_II_étendu
'  4) Passage de coordonnées géographiques d'un système géodésique vers
'     les coordonnées cartographiques planes d'une projection d'un autre système géodésique
'     - GEO_1 -> CAR_1 -> CAR_2 -> GEO_2 -> PLA_2
'     - E/S : lambda_1 et phi_1 (en radian) -> X_2 et Y_2 (en mètres)
'     - Exemple : WGS84 vers Lambert93
'  5) Passage de coordonnées géographiques d'un système géodésique vers
'     les coordonnées géographiques d'un autre système géodésique
'     - GEO_1 -> CAR_1 -> CAR_2 -> GEO_2
'     - E/S : lambda_1 et phi_1 -> lambda_2 et phi_2 (en radian)
'     - Exemple : WGS84 vers ED50
'
'Les étapes possibles sont donc :
'  1) PLA -> GEO     Fonction Alg04 - Passage d'une proj. Lambert vers coordonnées géographiques
'  2) GEO1 -> GEO2   Fonction Alg09 - Passage de coordonnées géographiques à des coord. cartésiennes
'                    Fonction Alg13 - Transformation d'un système géodésique à l'autre par coord. cartésiennes
'                    Fonction Alg12 - Passage de coordonnées cartésiennes à des coord. géographiques
'  3) GEO -> PLA     Fonction Alg03 - Passage de coord. géographiques en projection conique conforme de Lambert
' Remarque :
' - il est possible de définir d'autres transformations en utilisant un système
'   comme pivot si on ne connait pas la transformation directe
'   CAR_1 -> CAR_1bis (PIVOT) -> CAR_2
Private Enum eSchema
   eInitial = 0   'Système initial : Proj, Géo
   eFinal         'Système final: Géo, Proj
   bPLAtoGEO      'Etape 1
   eTypeTrans     'Type de tranformation pour bGEOtoGEO ou NULL si aucun
   bGEOtoPLA      'Etape 3
End Enum
'Paramètres de l'ellipsoïde du système initial et final
Private Type tEllipsoide
   da As Double         '1/2 grand axe de l'ellipsoïde (mètre)
   db As Double         '1/2 petit axe de l'ellipsoïde (mètre) - Non utilisé directement
   df As Double         'Facteur d'aplatissement  - Non utilisé directement
   de As Double         'Première excentricité de l'ellipsoïde de référence
   dh As Double         'Hauteur au dessus de l'ellipsoïde (en mètre)
End Type
'Paramètres de la projection
Private Type tProjection
   eType As eSCG
   dN As Double         'Exposant de la projection
   dc As Double         'Constante de la projection
   dXs As Double        'Coordonnées en projection du pôle
   dYs As Double
   dphi0 As Double      'Latitude du méridien d'origine (radian)
   dlambda0 As Double   'Longitude du méridien d'origine (ou central ?) (radian)
   dphi1 As Double      'Longitude du 1er parallèle automécoïque (radian)
   dphi2 As Double      'Longitude du 2ème parallèle automécoïque (radian)
   tEl As tEllipsoide   'Paramètres de l'ellipsoïde associée
End Type
'Paramètres de la transformation entre systèmes géo. cartésiens
Private Type tTransformation
   dTx As Double
   dTy As Double
   dTz As Double
   dRx As Double
   dRy As Double
   dRz As Double
   dFacteurEchelle As Double
End Type
'Paramètres du Système de Conversion Géodésique
Private Type tSCG
   avProcess As Variant
   tInitial As tProjection
   tTrans As tTransformation
   tFinal As tProjection
End Type
Private Type t_XYZ
   dX As Double
   dY As Double
   dZ As Double
End Type
Private gdpi As Double
Private gbInit As Boolean
Private gsErreur As String
Private gtXYZ As t_XYZ
Private gtSCG As tSCG
Private Sub class_initialize()
   gdpi = 4 * Atn(1)     'Calcul de pi
End Sub
'Sélection du type de conversion
Public Sub TypeConversion(ByVal eDe As eSCG, ByVal eVers As eSCG)
   Dim tTmpSCG As tSCG
   Dim tTmpXYZ As t_XYZ
   If eDe <> eVers Then
      With tTmpSCG
         'Récupère le processus de conversion
         .avProcess = SetConversion(eDe, eVers)
         'Conversion possible
         If Not IsNull(.avProcess) Then
            'Définir les systèmes
            SetProjection .tInitial, eDe
            SetProjection .tFinal, eVers
            'Définir l'éventuelle transformation
            If Not IsNull(.avProcess(eSchema.eTypeTrans)) Then
               SetTransformation .tTrans, .avProcess(eSchema.eTypeTrans)
            End If
            gsErreur = vbNullString
            gbInit = True
         Else
            gsErreur = "Conversion non définie..."
            gbInit = False
         End If
      End With
   Else
      gsErreur = "Pas de conversion nécessaire..."
      gbInit = False
   End If
   gtSCG = tTmpSCG
   gtXYZ = tTmpXYZ
End Sub
'Conversion à partir des mètres ou des radians selon le système de conversion
Public Sub CalculSCG(ByVal dX As Double, dY As Double)
   Dim tXYZ As t_XYZ
   With tXYZ
      If gbInit Then
         'Si passage de coordonnées planes vers geographiques
         If gtSCG.avProcess(eSchema.bPLAtoGEO) Then
            tXYZ = alg04(dX, dY, gtSCG.tInitial)
         Else
            'dx : Tenir compte du méridien d'origine
            .dX = dX + gtSCG.tInitial.dlambda0
            .dY = dY
         End If
         'Si passage de GEO1 vers GEO2
         If Not IsNull(gtSCG.avProcess(eSchema.eTypeTrans)) Then
            tXYZ = alg09(.dX, .dY, gtSCG.tInitial.tEl)   'GEO1 -> CAR1
            tXYZ = alg13(tXYZ, gtSCG.tTrans)           'CAR1 -> CAR2
            tXYZ = alg12(tXYZ, gtSCG.tFinal.tEl)       'CAR2 -> GEO2
         End If
         'Si passage de GEO vers PLA
         If gtSCG.avProcess(eSchema.bGEOtoPLA) Then
            tXYZ = alg03(.dX, .dY, gtSCG.tFinal)
         Else
            'dx : Tenir compte du méridien d'origine du système final
            .dX = .dX - gtSCG.tFinal.dlambda0
         End If
      Else
         gsErreur = "Système de conversion non initialisé..."
      End If
      gtXYZ.dX = .dX
      gtXYZ.dY = .dY
      gtXYZ.dZ = .dZ
   End With
End Sub
'Retourne le résultat : X  en mètre ou en radian (longitude)
Public Property Get X() As Double
   X = gtXYZ.dX
End Property
'Retourne le résultat : Y  en mètre ou en radian (latitude)
Public Property Get Y() As Double
   Y = gtXYZ.dY
End Property
Public Property Get pi() As Double
   pi = gdpi
End Property
Public Property Get IsErreur() As Boolean
   If Len(gsErreur) Then IsErreur = True
End Property
Public Property Get Erreur() As String
   Erreur = gsErreur
End Property
'alg01 - Appel par alg03 et alg54 - Calcul de la latitude isométrique à partir de la latitude
Private Function alg01(ByVal dphi As Double, ByVal de As Double) As Double
   Dim dTmp As Double
   dTmp = de * Sin(dphi)
   alg01 = Log(Tan(gdpi / 4 + dphi / 2) * ((1 - dTmp) / (1 + dTmp)) ^ (de / 2))
End Function
'alg02 - Appel par algo04 - Calcul la latitude à partir de la latitude isométrique
Private Function alg02(ByVal dphiIso As Double, ByVal de As Double, _
                       Optional ByVal depsilon As Double = 0.0000000001) As Double
   Dim dCurphi As Double, dLastphi As Double
   dCurphi = 2 * Atn(Exp(dphiIso)) - gdpi / 2
   Do
      dLastphi = dCurphi
      dCurphi = 2 * Atn(((1 + de * Sin(dLastphi)) / (1 - de * Sin(dLastphi))) ^ (de / 2) * Exp(dphiIso)) - gdpi / 2
   Loop Until Abs(dCurphi - dLastphi) < depsilon
   alg02 = dCurphi
End Function
'alg03 - Transformation de coordonnées géographiques en projection conique conforme de Lambert
'Utilise la variable globale gtSCG pour les constantes de la projection finale
Private Function alg03(ByVal dlambda As Double, ByVal dphi As Double, _
                       ByRef tProj As tProjection) As t_XYZ
   Dim dphiIso As Double, dTmp1 As Double, dtheta As Double
   With tProj
      dphiIso = alg01(dphi, .tEl.de)
      dTmp1 = .dc * Exp(-.dN * dphiIso)
      dtheta = .dN * (dlambda - .dlambda0)
      alg03.dX = .dXs + dTmp1 * Sin(dtheta)
      alg03.dY = .dYs - dTmp1 * Cos(dtheta)
   End With
End Function
'alg04 - Passage d'une projection Lambert vers des coordonnées géographiques
'Utilise la variable globale gtSCG pour les constantes de la projection initiale
Private Function alg04(ByVal dX As Double, ByVal dY As Double, _
                       ByRef tProj As tProjection, _
                       Optional ByVal depsilon As Double = 0.0000000001) As t_XYZ
   Dim dR As Double, dgamma As Double, dphiIso As Double
   With tProj
      dR = Sqr((dX - .dXs) ^ 2 + (dY - .dYs) ^ 2)
      dgamma = Atn((dX - .dXs) / (.dYs - dY))
      dphiIso = -1 / .dN * Log(Abs(dR / .dc))
      alg04.dX = .dlambda0 + dgamma / .dN
      alg04.dY = alg02(dphiIso, .tEl.de)
   End With
End Function
'alg21 - Appel par algo09 et alg54
Private Function alg21(ByVal dphi As Double, ByVal da As Double, ByVal de As Double) As Double
   alg21 = da / Sqr(1 - de ^ 2 * Sin(dphi) ^ 2)
End Function
'alg09 - Passage de coordonnées géographiques à cartésiennes
'Utilise la variable globale gtSCG pour l'ellipsoïde initiale
Private Function alg09(ByVal dlambda As Double, ByVal dphi As Double, _
                       ByRef tEl As tEllipsoide) As t_XYZ
   Dim dN As Double
   dlambda = dlambda
   With tEl
      dN = alg21(dphi, .da, .de)
      alg09.dX = (dN + .dh) * Cos(dphi) * Cos(dlambda)
      alg09.dY = (dN + .dh) * Cos(dphi) * Sin(dlambda)
      alg09.dZ = (dN * (1 - .de ^ 2) + .dh) * Sin(dphi)
   End With
End Function
'alg12 - Passage de coordonnées cartésiennes à géographiques
'Utilise la variable globale gtSCG pour l'ellipsoïde finale
Private Function alg12(ByRef tXYZ As t_XYZ, _
                       ByRef tEl As tEllipsoide, _
                       Optional ByVal depsilon As Double = 0.0000000001) As t_XYZ
   Dim dCurphi As Double, dLastphi As Double, dTmp1 As Double, dTmp2 As Double
   Dim deCarre As Double, da As Double
   With tEl
      deCarre = .de ^ 2
      da = .da
   End With
   With tXYZ
      alg12.dX = Atn(.dY / .dX)
      dTmp1 = Sqr(.dX ^ 2 + .dY ^ 2)
      dTmp2 = da * deCarre
      dCurphi = Atn(.dZ / (dTmp1 * (1 - (dTmp2 / Sqr(.dX ^ 2 + .dY ^ 2 + .dZ ^ 2)))))
      Do
         dLastphi = dCurphi
         dCurphi = Atn(.dZ / (dTmp1 * (1 - (dTmp2 * Cos(dLastphi) / _
                                            (dTmp1 * Sqr(1 - deCarre * Sin(dLastphi) ^ 2))))))
      Loop Until Abs(dCurphi - dLastphi) < depsilon
      alg12.dY = dCurphi
      alg12.dZ = dTmp1 / Cos(dCurphi) - (da / Sqr(1 - deCarre * Sin(dLastphi) ^ 2))
   End With
End Function
'alg13 - Passage d'un SG à l'autre par coordonnées cartésiennes
'Utilise la variable globale gtSCG pour la transformation
'Modif algo : passage d'un facteur d'échelle additionné de 1
Private Function alg13(ByRef tXYZ As t_XYZ, ByRef tTrans As tTransformation) As t_XYZ
   With tTrans
      alg13.dX = .dTx + tXYZ.dX * .dFacteurEchelle + tXYZ.dZ * .dRy - tXYZ.dY * .dRz
      alg13.dY = .dTy + tXYZ.dY * .dFacteurEchelle + tXYZ.dX * .dRz - tXYZ.dZ * .dRx
      alg13.dZ = .dTz + tXYZ.dZ * .dFacteurEchelle + tXYZ.dY * .dRx - tXYZ.dX * .dRy
   End With
End Function
'alg54 - Permet de calculer les constantes d'une projection conique conforme dans le cas sécant
Private Sub alg54(ByRef tProj As tProjection, ByVal dX0 As Double, ByVal dY0 As Double)
   Dim dTmp1 As Double, dLatIso1 As Double
   With tProj
      dTmp1 = alg21(.dphi1, .tEl.da, .tEl.de) * Cos(.dphi1)
      dLatIso1 = alg01(.dphi1, .tEl.de)
      .dN = Log(alg21(.dphi2, .tEl.da, .tEl.de) * Cos(.dphi2) / dTmp1) / _
            (dLatIso1 - alg01(.dphi2, .tEl.de))
      .dc = (dTmp1 / .dN) * Exp(.dN * dLatIso1)
      .dXs = dX0
      If Arrondi(.dphi0, 9) <> Arrondi(gdpi / 2, 9) Then
         .dYs = dY0 + .dc * Exp(-.dN * alg01(.dphi0, .tEl.de))
      Else
         .dYs = dY0
      End If
   End With
End Sub
Private Sub SetProjection(ByRef tProj As tProjection, ByVal eType As eSCG)
   With tProj
      .eType = eType
      'Paramètres du Système géodésique
      .tEl.dh = 100 'Hauteur au dessus de l'ellipsoïde, valeur par défaut
      Select Case eType
      Case eSCG.NTF_Lambert_I, _
           eSCG.NTF_Lambert_II, _
           eSCG.NTF_Lambert_III, _
           eSCG.NTF_Lambert_IV, _
           eSCG.NTF_Lambert_II_Etendu, _
           eSCG.NTF_Paris_Geo                      'Ellipsoïde de Clarke 1880
         .dlambda0 = DMSToRadian(2, 20, 14.025)    'Paris = 2°20'14,025 E Greenwich
         .tEl.da = 6378249.2
         .tEl.db = 6356515#
         .tEl.de = CalculPremiereExcentricite(.tEl.da, .tEl.db)   '0.08248325676
      Case eSCG.RGF93_Lambert_93, eSCG.RGF93_Geo   'Ellipsoïde IAG_GRS80
         .dlambda0 = DegreDecToRadian(3)
         .tEl.da = 6378137
         .tEl.df = 1 / 298.257222101
         .tEl.db = CalculDemiPetitAxe(.tEl.da, .tEl.df)
         .tEl.de = CalculPremiereExcentricite(.tEl.da, .tEl.db)
      Case eSCG.WGS84_Geo                          'Ellipsoïde WGS84
         .dlambda0 = 0
         .tEl.da = 6378137
         .tEl.df = 1 / 298.257223563
         .tEl.db = CalculDemiPetitAxe(.tEl.da, .tEl.df)
         .tEl.de = CalculPremiereExcentricite(.tEl.da, .tEl.db)   '0.08181919106
      Case eSCG.ETRS89_Geo, eSCG.ETRS89_Lambert08  'Ellipsoïde GRS80
         .tEl.da = 6378137
         .tEl.df = 1 / 298.257222101
         .tEl.db = CalculDemiPetitAxe(.tEl.da, .tEl.df)
         .tEl.de = CalculPremiereExcentricite(.tEl.da, .tEl.db)   '0.08181919106
      Case eSCG.ED50_Geo, eSCG.BD72_Geo, eSCG.BD72_Lambert72   'Ellipsoïde Hayford 1909 et International 1924 ?
         .tEl.da = 6378388
         .tEl.df = 1 / 297#
         .tEl.db = CalculDemiPetitAxe(.tEl.da, .tEl.df)
         .tEl.de = CalculPremiereExcentricite(.tEl.da, .tEl.db)   '0.08199188998
      End Select
      'Paramètres de la projection
      Select Case eType
      Case eSCG.NTF_Lambert_I            'Nord France
         .dphi0 = GradeToRadian(55#)
         .dphi1 = DMSToRadian(48, 35, 54.682)
         .dphi2 = DMSToRadian(50, 23, 45.282)
         .dN = 0.7604059656
         .dc = 11603796.98
         .dXs = 600000#
         .dYs = 5657616.674
      Case eSCG.NTF_Lambert_II           'Centre France
         .dphi0 = GradeToRadian(52#)
         .dphi1 = DMSToRadian(45, 53, 56.108)
         .dphi2 = DMSToRadian(47, 41, 45.652)
         .dN = 0.7289686274
         .dc = 11745793.39
         .dXs = 600000#
         .dYs = 6199695.768
      Case eSCG.NTF_Lambert_III          'Sud France
         .dphi0 = GradeToRadian(49#)
         .dphi1 = DMSToRadian(43, 11, 57.449)
         .dphi2 = DMSToRadian(44, 59, 45.938)
         .dN = 0.6959127966
         .dc = 11947992.52
         .dXs = 600000#
         .dYs = 6791905.085
      Case eSCG.NTF_Lambert_IV           'Corse
         .dphi0 = GradeToRadian(46.85)
         .dphi1 = DMSToRadian(41, 33, 37.396)
         .dphi2 = DMSToRadian(42, 46, 3.588)
         .dN = 0.6712679322
         .dc = 12136281.99
         .dXs = 234.358
         .dYs = 7239161.542
      Case eSCG.NTF_Lambert_II_Etendu    'France entière
         .dphi0 = GradeToRadian(52#)
         .dphi1 = DMSToRadian(45, 53, 56.108)
         .dphi2 = DMSToRadian(47, 41, 45.652)
         .dN = 0.7289686274
         .dc = 11745793.39
         .dXs = 600000#
         .dYs = 8199695.768
      Case eSCG.RGF93_Lambert_93          'France entière
         .dlambda0 = DegreDecToRadian(3)
         .dphi0 = DMSToRadian(46, 30)
         .dphi1 = DegreDecToRadian(44)
         .dphi2 = DegreDecToRadian(49)
         .dN = 0.725607765
         .dc = 11754255.426
         .dXs = 700000#
         .dYs = 12655612.05
      Case eSCG.BD72_Lambert72            'Belgique
         .dlambda0 = DMSToRadian(4, 22, 2.952)
         .dphi0 = DegreDecToRadian(90)
         .dphi1 = DMSToRadian(49, 50, 0.00204)
         .dphi2 = DMSToRadian(51, 10, 0.00204)
         'Calcul les constantes
         alg54 tProj, 150000.013, 5400088.438
      Case eSCG.ETRS89_Lambert08          'Belgique
         .dlambda0 = DMSToRadian(4, 21, 33.177)
         .dphi0 = DMSToRadian(50, 47, 52.134)
         .dphi1 = DMSToRadian(49, 50)
         .dphi2 = DMSToRadian(51, 10)
         'Calcul les constantes
         alg54 tProj, 649328#, 665262#
      End Select
   End With
End Sub
'Paramètres de transformation d'un système à l'autre
Private Sub SetTransformation(ByRef tTrans As tTransformation, ByVal eTrans As eTypeTrans)
   With tTrans
      'Selon convention de l'IERS : Coordinate Frame Rotation ?
      'Par défaut
      .dFacteurEchelle = 1
      Select Case eTrans
      Case eTypeTrans.NTFtoRGF93, eTypeTrans.NTFtoWGS84, _
           eTypeTrans.RGF93toNTF, eTypeTrans.WGS84toNTF
         .dTx = -168
         .dTy = -60
         .dTz = 320
         If eTrans = eTypeTrans.RGF93toNTF Or eTrans = eTypeTrans.WGS84toNTF Then
            TransformationInverse tTrans
         End If
      Case eTypeTrans.NTFtoED50, eTypeTrans.ED50toNTF
         .dTx = -84
         .dTy = 37
         .dTz = 437
         If eTrans = eTypeTrans.ED50toNTF Then
            TransformationInverse tTrans
         End If
      Case eTypeTrans.ED50toWGS84, eTypeTrans.ED50toRGF93, _
           eTypeTrans.RGF93toED50, eTypeTrans.WGS84toED50
         .dTx = -84
         .dTy = -97
         .dTz = -117
         If eTrans = eTypeTrans.RGF93toED50 Or eTrans = eTypeTrans.WGS84toED50 Then
            TransformationInverse tTrans
         End If
      Case eTypeTrans.ETRS89toBD72, eTypeTrans.BD72toETRS89
         .dTx = 106.868628
         .dTy = -52.297783
         .dTz = 103.723893
         .dRx = DMSToRadian(0, 0, 0.33657, True)
         .dRy = DMSToRadian(0, 0, 0.456955)
         .dRz = DMSToRadian(0, 0, 1.842183, True)
         .dFacteurEchelle = 1 + 0.0000012747
         If eTrans = eTypeTrans.BD72toETRS89 Then
            TransformationInverse tTrans
         End If
      Case eTypeTrans.RGF93toWGS84, eTypeTrans.WGS84toRGF93
         'pas de transformation entre ces systèmes
      End Select
   End With
End Sub
'Détermine les paramètres de la transformation inverse
Private Sub TransformationInverse(ByRef tTrans As tTransformation)
   With tTrans
      .dTx = -.dTx
      .dTy = -.dTy
      .dTz = -.dTz
      .dRx = -.dRx
      .dRy = -.dRy
      .dRz = -.dRz
      If .dFacteurEchelle <> 0 Then .dFacteurEchelle = 1 / .dFacteurEchelle
   End With
End Sub

 
 
Private Function SetConversion(ByVal eDe As eSCG, ByVal eVers As eSCG) As Variant
   Dim avC(0 To 63) As Variant
   Dim i As Integer
   avC(0) = VBA.Array(eSCG.NTF_Lambert_I, eSCG.NTF_Paris_Geo, True, Null, False)
   avC(1) = VBA.Array(eSCG.NTF_Lambert_I, eSCG.NTF_Lambert_II_Etendu, True, Null, True)
   avC(2) = VBA.Array(eSCG.NTF_Lambert_I, eSCG.RGF93_Geo, True, eTypeTrans.NTFtoRGF93, False)
   avC(3) = VBA.Array(eSCG.NTF_Lambert_I, eSCG.RGF93_Lambert_93, True, eTypeTrans.NTFtoRGF93, True)
   avC(4) = VBA.Array(eSCG.NTF_Lambert_I, eSCG.ED50_Geo, True, eTypeTrans.NTFtoED50, False)
   avC(5) = VBA.Array(eSCG.NTF_Lambert_I, eSCG.WGS84_Geo, True, eTypeTrans.NTFtoWGS84, False)
   avC(6) = VBA.Array(eSCG.NTF_Lambert_II, eSCG.NTF_Paris_Geo, True, Null, False)
   avC(7) = VBA.Array(eSCG.NTF_Lambert_II, eSCG.NTF_Lambert_II_Etendu, True, Null, True)
   avC(8) = VBA.Array(eSCG.NTF_Lambert_II, eSCG.RGF93_Geo, True, eTypeTrans.NTFtoRGF93, False)
   avC(9) = VBA.Array(eSCG.NTF_Lambert_II, eSCG.RGF93_Lambert_93, True, eTypeTrans.NTFtoRGF93, True)
   avC(10) = VBA.Array(eSCG.NTF_Lambert_II, eSCG.ED50_Geo, True, eTypeTrans.NTFtoED50, False)
   avC(11) = VBA.Array(eSCG.NTF_Lambert_II, eSCG.WGS84_Geo, True, eTypeTrans.NTFtoWGS84, False)
   avC(12) = VBA.Array(eSCG.NTF_Lambert_III, eSCG.NTF_Paris_Geo, True, Null, False)
   avC(13) = VBA.Array(eSCG.NTF_Lambert_III, eSCG.NTF_Lambert_II_Etendu, True, Null, True)
   avC(14) = VBA.Array(eSCG.NTF_Lambert_III, eSCG.RGF93_Geo, True, eTypeTrans.NTFtoRGF93, False)
   avC(15) = VBA.Array(eSCG.NTF_Lambert_III, eSCG.RGF93_Lambert_93, True, eTypeTrans.NTFtoRGF93, True)
   avC(16) = VBA.Array(eSCG.NTF_Lambert_III, eSCG.ED50_Geo, True, eTypeTrans.NTFtoED50, False)
   avC(17) = VBA.Array(eSCG.NTF_Lambert_III, eSCG.WGS84_Geo, True, eTypeTrans.NTFtoWGS84, False)
   avC(18) = VBA.Array(eSCG.NTF_Lambert_IV, eSCG.NTF_Paris_Geo, True, Null, False)
   avC(19) = VBA.Array(eSCG.NTF_Lambert_IV, eSCG.NTF_Lambert_II_Etendu, True, Null, True)
   avC(20) = VBA.Array(eSCG.NTF_Lambert_IV, eSCG.RGF93_Geo, True, eTypeTrans.NTFtoRGF93, False)
   avC(21) = VBA.Array(eSCG.NTF_Lambert_IV, eSCG.RGF93_Lambert_93, True, eTypeTrans.NTFtoRGF93, True)
   avC(22) = VBA.Array(eSCG.NTF_Lambert_IV, eSCG.ED50_Geo, True, eTypeTrans.NTFtoED50, False)
   avC(23) = VBA.Array(eSCG.NTF_Lambert_IV, eSCG.WGS84_Geo, True, eTypeTrans.NTFtoWGS84, False)
   avC(24) = VBA.Array(eSCG.NTF_Lambert_II_Etendu, eSCG.NTF_Paris_Geo, True, Null, False)
   avC(25) = VBA.Array(eSCG.NTF_Lambert_II_Etendu, eSCG.RGF93_Geo, True, eTypeTrans.NTFtoRGF93, False)
   avC(26) = VBA.Array(eSCG.NTF_Lambert_II_Etendu, eSCG.RGF93_Lambert_93, True, eTypeTrans.NTFtoRGF93, True)
   avC(27) = VBA.Array(eSCG.NTF_Lambert_II_Etendu, eSCG.ED50_Geo, True, eTypeTrans.NTFtoED50, False)
   avC(28) = VBA.Array(eSCG.NTF_Lambert_II_Etendu, eSCG.WGS84_Geo, True, eTypeTrans.NTFtoWGS84, False)
   avC(29) = VBA.Array(eSCG.NTF_Paris_Geo, eSCG.NTF_Lambert_II_Etendu, False, Null, True)
   avC(30) = VBA.Array(eSCG.NTF_Paris_Geo, eSCG.RGF93_Geo, False, eTypeTrans.NTFtoRGF93, False)
   avC(31) = VBA.Array(eSCG.NTF_Paris_Geo, eSCG.RGF93_Lambert_93, False, eTypeTrans.NTFtoRGF93, True)
   avC(32) = VBA.Array(eSCG.NTF_Paris_Geo, eSCG.ED50_Geo, False, eTypeTrans.NTFtoED50, False)
   avC(33) = VBA.Array(eSCG.NTF_Paris_Geo, eSCG.WGS84_Geo, False, eTypeTrans.NTFtoWGS84, False)
   avC(34) = VBA.Array(eSCG.RGF93_Geo, eSCG.NTF_Lambert_II_Etendu, False, eTypeTrans.RGF93toNTF, True)
   avC(35) = VBA.Array(eSCG.RGF93_Geo, eSCG.NTF_Paris_Geo, False, eTypeTrans.RGF93toNTF, False)
   avC(36) = VBA.Array(eSCG.RGF93_Geo, eSCG.RGF93_Lambert_93, False, Null, True)
   avC(37) = VBA.Array(eSCG.RGF93_Geo, eSCG.ED50_Geo, False, eTypeTrans.RGF93toED50, False)
   avC(38) = VBA.Array(eSCG.RGF93_Geo, eSCG.WGS84_Geo, False, eTypeTrans.RGF93toWGS84, False)
   avC(39) = VBA.Array(eSCG.RGF93_Lambert_93, eSCG.NTF_Paris_Geo, True, eTypeTrans.RGF93toNTF, False)
   avC(40) = VBA.Array(eSCG.RGF93_Lambert_93, eSCG.NTF_Lambert_II_Etendu, True, eTypeTrans.RGF93toNTF, True)
   avC(41) = VBA.Array(eSCG.RGF93_Lambert_93, eSCG.RGF93_Geo, True, Null, False)
   avC(42) = VBA.Array(eSCG.RGF93_Lambert_93, eSCG.ED50_Geo, True, eTypeTrans.RGF93toED50, False)
   avC(43) = VBA.Array(eSCG.RGF93_Lambert_93, eSCG.WGS84_Geo, True, eTypeTrans.RGF93toWGS84, False)
   avC(44) = VBA.Array(eSCG.ED50_Geo, eSCG.NTF_Paris_Geo, False, eTypeTrans.ED50toNTF, False)
   avC(45) = VBA.Array(eSCG.ED50_Geo, eSCG.NTF_Lambert_II_Etendu, False, eTypeTrans.ED50toNTF, True)
   avC(46) = VBA.Array(eSCG.ED50_Geo, eSCG.RGF93_Geo, False, eTypeTrans.ED50toRGF93, False)
   avC(47) = VBA.Array(eSCG.ED50_Geo, eSCG.RGF93_Lambert_93, False, eTypeTrans.ED50toRGF93, True)
   avC(48) = VBA.Array(eSCG.ED50_Geo, eSCG.WGS84_Geo, False, eTypeTrans.ED50toWGS84, False)
   avC(49) = VBA.Array(eSCG.WGS84_Geo, eSCG.NTF_Paris_Geo, False, eTypeTrans.WGS84toNTF, False)
   avC(50) = VBA.Array(eSCG.WGS84_Geo, eSCG.NTF_Lambert_II_Etendu, False, eTypeTrans.WGS84toNTF, True)
   avC(51) = VBA.Array(eSCG.WGS84_Geo, eSCG.RGF93_Geo, False, eTypeTrans.WGS84toRGF93, False)
   avC(52) = VBA.Array(eSCG.WGS84_Geo, eSCG.RGF93_Lambert_93, False, eTypeTrans.WGS84toRGF93, True)
   avC(53) = VBA.Array(eSCG.WGS84_Geo, eSCG.ED50_Geo, False, eTypeTrans.WGS84toED50, False)
   avC(54) = VBA.Array(eSCG.BD72_Lambert72, eSCG.ETRS89_Lambert08, True, eTypeTrans.BD72toETRS89, True)
   avC(55) = VBA.Array(eSCG.BD72_Lambert72, eSCG.BD72_Geo, True, Null, False)
   avC(56) = VBA.Array(eSCG.BD72_Lambert72, eSCG.ETRS89_Geo, True, eTypeTrans.BD72toETRS89, False)
   avC(57) = VBA.Array(eSCG.BD72_Geo, eSCG.ETRS89_Geo, False, eTypeTrans.BD72toETRS89, False)
   avC(58) = VBA.Array(eSCG.ETRS89_Lambert08, eSCG.BD72_Lambert72, True, eTypeTrans.ETRS89toBD72, True)
   avC(59) = VBA.Array(eSCG.ETRS89_Lambert08, eSCG.BD72_Geo, True, eTypeTrans.ETRS89toBD72, False)
   avC(60) = VBA.Array(eSCG.ETRS89_Lambert08, eSCG.ETRS89_Geo, True, Null, False)
   avC(61) = VBA.Array(eSCG.ETRS89_Geo, eSCG.BD72_Lambert72, False, eTypeTrans.ETRS89toBD72, True)
   avC(62) = VBA.Array(eSCG.ETRS89_Geo, eSCG.BD72_Geo, False, eTypeTrans.ETRS89toBD72, False)
   avC(63) = VBA.Array(eSCG.ETRS89_Geo, eSCG.ETRS89_Lambert08, False, Null, True)
   For i = 0 To UBound(avC)
      If avC(i)(eSchema.eInitial) = eDe And avC(i)(eSchema.eFinal) = eVers Then
         SetConversion = avC(i)
         Exit For
      End If
   Next i
End Function
Private Function CalculDemiPetitAxe(ByVal da As Double, ByVal df As Double) As Double
   CalculDemiPetitAxe = da * (1 - df)
End Function
Private Function CalculPremiereExcentricite(ByVal da As Double, ByVal db As Double) As Double
   Dim daCarre As Double
   daCarre = da ^ 2
   CalculPremiereExcentricite = Sqr((daCarre - db ^ 2) / daCarre)
End Function
Public Function GradeToRadian(ByVal dAngle As Double)
   GradeToRadian = gdpi * dAngle / 200
End Function
Public Function RadianToGrade(ByVal dAngle As Double)
   RadianToGrade = 200 * dAngle / gdpi
End Function
Public Function DegreDecToRadian(ByVal dAngle As Double)
   DegreDecToRadian = gdpi * dAngle / 180
End Function
Public Function RadianToDegreDec(ByVal dAngle As Double)
   RadianToDegreDec = 180 * dAngle / gdpi
End Function
Public Function DMSToDegreDec(ByVal iDegre As Integer, Optional ByVal byMin As Byte = 0, _
                              Optional ByVal fSec As Single = 0, _
                              Optional bNegatif As Boolean = False) As Double
   If byMin < 60 And fSec < 60 Then
      DMSToDegreDec = Abs(iDegre) + byMin / 60 + Abs(fSec) / 3600
      If bNegatif Then DMSToDegreDec = -DMSToDegreDec
   End If
End Function
Public Function AngleDMSToDegreDec(ByVal dAngle As Double) As Double
   Dim fSec As Single
   Dim iDegre As Integer, iMin As Integer, iSigne As Integer
   iSigne = Sgn(dAngle)
   dAngle = Abs(dAngle)
   iDegre = Int(dAngle)
   iMin = Int((dAngle - iDegre) * 100)
   fSec = (dAngle - iDegre - iMin / 100) * 10000
   AngleDMSToDegreDec = (iDegre + iMin / 60 + fSec / 3600) * iSigne
End Function
Public Function DMSToRadian(ByVal iDegre As Integer, Optional ByVal byMin As Byte = 0, _
                            Optional ByVal fSec As Single = 0, _
                            Optional bNegatif As Boolean = False) As Double
   DMSToRadian = DegreDecToRadian(DMSToDegreDec(iDegre, byMin, fSec, bNegatif))
End Function
Public Function AngleDMSToRadian(ByVal dAngle As Double) As Double
   AngleDMSToRadian = DegreDecToRadian(AngleDMSToDegreDec(dAngle))
End Function
Public Function DegreDecToAngleDMS(ByVal dAngle As Double) As Double
   Dim iDegre As Integer, iMin As Integer, iSigne As Integer
   Dim fSec As Single
   iSigne = Sgn(dAngle)
   dAngle = Abs(dAngle)
   iDegre = Int(dAngle)
   iMin = Int((dAngle - iDegre) * 60)
   fSec = (dAngle - iDegre - iMin / 60) * 3600
   DegreDecToAngleDMS = Arrondi((iDegre + (iMin + fSec / 100) / 100) * iSigne, 7)
End Function
Public Function AngleDMSToString(ByVal dAngle As Double, ByVal bLatitude As Boolean) As String
   Dim iDegre As Integer, iMin As Integer, iSigne As Integer
   Dim fSec As Single
   Dim s As String
   iSigne = Sgn(dAngle)
   dAngle = Abs(dAngle)
   iDegre = Int(dAngle)
   iMin = Int((dAngle - iDegre) * 100)
   fSec = (dAngle - iDegre - iMin / 100) * 10000
   s = iDegre & "°" & format(iMin, "00'") & format(fSec, "00.0##''")
   If iSigne < 0 Then
      s = s & IIf(bLatitude, "S", "W")
   Else
      s = s & IIf(bLatitude, "N", "E")
   End If
   AngleDMSToString = s
End Function
Public Function RadianToStringDMS(ByVal dAngle As Double, ByVal bLatitude As Boolean) As String
   RadianToStringDMS = AngleDMSToString(DegreDecToAngleDMS(RadianToDegreDec(dAngle)), bLatitude)
End Function
Public Function Arrondi(ByVal dVal As Double, Optional ByVal iDec As Integer = 0) As Double
   Arrondi = Int((dVal * (10 ^ iDec)) + 0.5) / (10 ^ iDec)
End Function
Public Function DistanceKm(ByVal dRadLon1 As Double, ByVal dRadLat1 As Double, _
                           ByVal dRadLon2 As Double, ByVal dRadLat2 As Double) As Double
   DistanceKm = 6371 * ArcCosRad(Cos(dRadLat1) * Cos(dRadLat2) * Cos(dRadLon2 - dRadLon1) + _
                                 (Sin(dRadLat1) * Sin(dRadLat2)))
End Function
Private Function ArcCosRad(dRadian As Double) As Double
   If dRadian > -1 And dRadian < 1 Then
      ArcCosRad = Atn(-dRadian / Sqr(-dRadian * dRadian + 1)) + gdpi / 2
   ElseIf dRadian = -1 Then
      ArcCosRad = gdpi
   End If
End Function
#If CCTEST Then
Public Function TestAngles()
   Dim sTabs As String
   sTabs = vbTab & vbTab
Debug.Print "--- Vérification des conversions d'angles ---"
Debug.Print vbTab & "+ Grade -> Radian"
Debug.Print sTabs & "Entrée : " & 0 & " - Attendu : " & 0 & " - Obtenu : " & GradeToRadian(0)
Debug.Print sTabs & "Entrée : " & 200 & " - Attendu : " & gdpi & " - Obtenu : " & GradeToRadian(200)
Debug.Print sTabs & "Entrée : " & -200 & " - Attendu : " & -gdpi & " - Obtenu : " & GradeToRadian(-200)
Debug.Print sTabs & "Entrée : " & 600 & " - Attendu : " & (3 * gdpi) & " - Obtenu : " & GradeToRadian(600)
Debug.Print vbTab & "+ Radian -> Grade"
Debug.Print sTabs & "Entrée : " & 0 & " - Attendu : " & 0 & " - Obtenu : " & RadianToGrade(0)
Debug.Print sTabs & "Entrée : " & "pi" & " - Attendu : " & 200 & " - Obtenu : " & RadianToGrade(gdpi)
Debug.Print sTabs & "Entrée : " & "-pi" & " - Attendu : " & -200 & " - Obtenu : " & RadianToGrade(-gdpi)
Debug.Print sTabs & "Entrée : " & (3 * gdpi) & " - Attendu : " & 600 & " - Obtenu : " & RadianToGrade(3 * gdpi)
Debug.Print vbTab & "+ Degré décimal -> Radian"
Debug.Print sTabs & "Entrée : " & 0 & " - Attendu : " & 0 & " - Obtenu : " & DegreDecToRadian(0)
Debug.Print sTabs & "Entrée : " & 180 & " - Attendu : " & "pi" & " - Obtenu : " & DegreDecToRadian(180)
Debug.Print sTabs & "Entrée : " & -180 & " - Attendu : " & "-pi" & " - Obtenu : " & DegreDecToRadian(-180)
Debug.Print sTabs & "Entrée : " & 540 & " - Attendu : " & (3 * gdpi) & " - Obtenu : " & DegreDecToRadian(540)
Debug.Print vbTab & "+ Radian -> Degré décimal"
Debug.Print sTabs & "Entrée : " & 0 & " - Attendu : " & 0 & " - Obtenu : " & RadianToDegreDec(0)
Debug.Print sTabs & "Entrée : " & "pi" & " - Attendu : " & 180 & " - Obtenu : " & RadianToDegreDec(gdpi)
Debug.Print sTabs & "Entrée : " & "-pi" & " - Attendu : " & -180 & " - Obtenu : " & RadianToDegreDec(-gdpi)
Debug.Print sTabs & "Entrée : " & (3 * gdpi) & " - Attendu : " & 540 & " - Obtenu : " & RadianToDegreDec(3 * gdpi)
Debug.Print vbTab & "+ DMS(Deg,Min,Sec) -> Degré décimal"
Debug.Print sTabs & "Entrée : " & 0 & " - Attendu : " & 0 & " - Obtenu : " & DMSToDegreDec(0)
Debug.Print sTabs & "Entrée : " & 180 & " - Attendu : " & 180 & " - Obtenu : " & DMSToDegreDec(180)
Debug.Print sTabs & "Entrée : " & "45°35'25.566''" & " - Attendu : " & 45.590435 & " - Obtenu : " & Arrondi(DMSToDegreDec(45, 35, 25.567), 6)
Debug.Print sTabs & "Entrée : " & "-45°35'25.566''" & " - Attendu : " & -45.590435 & " - Obtenu : " & Arrondi(DMSToDegreDec(-45, 35, 25.566, True), 6)
Debug.Print sTabs & "Entrée : " & "-45°75'25.566''" & " - Attendu : " & 0 & " car erreur ! - Obtenu : " & Arrondi(DMSToDegreDec(-45, 75, 25.566, True), 6)
Debug.Print sTabs & "Entrée : " & "-45°35'85.566''" & " - Attendu : " & 0 & " car erreur ! - Obtenu : " & Arrondi(DMSToDegreDec(-45, 35, 85.566, True), 6)
Debug.Print vbTab & "+ Angle DMS (Degre.mmssxxx) -> Degré décimal"
Debug.Print sTabs & "Entrée : " & 0 & " - Attendu : " & 0 & " - Obtenu : " & AngleDMSToDegreDec(0)
Debug.Print sTabs & "Entrée : " & 180 & " - Attendu : " & 180 & " - Obtenu : " & AngleDMSToDegreDec(180)
Debug.Print sTabs & "Entrée : " & 45.3525566 & " - Attendu : " & 45.590435 & " - Obtenu : " & Arrondi(AngleDMSToDegreDec(45.3525566), 6)
Debug.Print sTabs & "Entrée : " & -45.3525566 & " - Attendu : " & -45.590435 & " - Obtenu : " & Arrondi(AngleDMSToDegreDec(-45.3525566), 6)
Debug.Print vbTab & "+ Degré décimal -> Angle DMS (Degre.mmssxxx)"
Debug.Print sTabs & "Entrée : " & 0 & " - Attendu : " & 0 & " - Obtenu : " & DegreDecToAngleDMS(0)
Debug.Print sTabs & "Entrée : " & 180 & " - Attendu : " & 180 & " - Obtenu : " & DegreDecToAngleDMS(180)
Debug.Print sTabs & "Entrée : " & 45.590435 & " - Attendu : " & 45.3525566 & " - Obtenu : " & Arrondi(DegreDecToAngleDMS(45.590435), 7)
Debug.Print sTabs & "Entrée : " & -45.590435 & " - Attendu : " & -45.3525566 & " - Obtenu : " & Arrondi(DegreDecToAngleDMS(-45.590435), 7)
Debug.Print vbTab & "+ Angle DMS (Degre.mmssxxx) -> String"
Debug.Print sTabs & "Entrée : " & 0 & " Lat - Attendu : " & "0°00'00,0''N" & " - Obtenu : " & AngleDMSToString(0, True)
Debug.Print sTabs & "Entrée : " & 45 & " Lon - Attendu : " & "45°00'00,0''E" & " - Obtenu : " & AngleDMSToString(45, False)
Debug.Print sTabs & "Entrée : " & 45.590435 & " Lat - Attendu : " & "45°59'04,35''N" & " - Obtenu : " & AngleDMSToString(45.590435, True)
Debug.Print sTabs & "Entrée : " & -45.3525566 & " Lat - Attendu : " & "45°35'25,566''S" & " - Obtenu : " & AngleDMSToString(-45.3525566, True)
Debug.Print "---"
End Function
Public Sub TestConversions()
Debug.Print "--- Vérification de certaines conversions ---"
Debug.Print vbTab & "+ NTF méridien de Paris -> ED50 Greenwich"
   Test NTF_Paris_Geo, ED50_Geo, 6, 54, "7°44'16,4''", "48°36'03,3''", gr, dms
Debug.Print vbTab & "+ NTF méridien de Paris -> WGS84"
   Test NTF_Paris_Geo, WGS84_Geo, 6, 54, "7°44'12,2''", "48°35'59,9''", gr, dms
Debug.Print vbTab & "+ NTF méridien de Paris -> NTF Lambert II étendu"
   Test NTF_Paris_Geo, NTF_Lambert_II_Etendu, 6, 54, "998137m", "2413822m", gr, m, 0
Debug.Print vbTab & "+ NTF méridien de Paris -> RGF Lambert 93"
   Test NTF_Paris_Geo, RGF93_Lambert_93, 6, 54, "1049052m", "6843777m", gr, m, 0
Debug.Print vbTab & "+ NTF Lambert I -> ED50 Greenwich"
   Test NTF_Lambert_I, ED50_Geo, 997960, 114185, "7°44'16,4''", "48°36'03,3''", m, dms
Debug.Print vbTab & "+ NTF Lambert I -> WGS84"
   Test NTF_Lambert_I, WGS84_Geo, 997960, 114185, "7°44'12,2''", "48°35'59,9''", m, dms
Debug.Print vbTab & "+ NTF Lambert I -> NTF Lambert II étendu"
   Test NTF_Lambert_I, NTF_Lambert_II_Etendu, 997960, 114185, "998137m", "2413822m", m, m, 0
Debug.Print vbTab & "+ NTF Lambert I -> RGF Lambert 93"
   Test NTF_Lambert_I, RGF93_Lambert_93, 997960, 114185, "1049052m", "6843777m", m, m, 0
Debug.Print vbTab & "+ NTF Lambert II étendu -> ED50 Greenwich"
   Test NTF_Lambert_II_Etendu, ED50_Geo, 998137, 2413822, "7°44'16,4''", "48°36'03,3''", m, dms
Debug.Print vbTab & "+ NTF Lambert II étendu -> WGS84"
   Test NTF_Lambert_II_Etendu, WGS84_Geo, 998137, 2413822, "7°44'12,2''", "48°35'59,9''", m, dms
Debug.Print vbTab & "+ NTF Lambert II étendu -> NTF méridien de Paris"
   Test NTF_Lambert_II_Etendu, NTF_Paris_Geo, 998137, 2413822, "6gr", "54gr", m, gr, 0
Debug.Print vbTab & "+ NTF Lambert II étendu -> RGF Lambert 93"
   Test NTF_Lambert_II_Etendu, RGF93_Lambert_93, 998137, 2413822, "1049052m", "6843777m", m, m, 1
Debug.Print vbTab & "+ RGF Lambert 93 -> NTF Lambert II étendu"
   Test RGF93_Lambert_93, NTF_Lambert_II_Etendu, 1049052, 6843777, " 998137m", "2413822m", m, m, 1
Debug.Print vbTab & "+ BD72 Lambert 72 -> BD72"
   Test BD72_Lambert72, BD72_Geo, 235000, 45000, "5°32'45.48188''", "49°42'40.06796''", m, dms
Debug.Print vbTab & "+ BD72 Lambert 72 -> ETRS89"
   Test BD72_Lambert72, ETRS89_Geo, 235000, 45000, "5°32'50.09529''", "49°42'37.96785''", m, dms
Debug.Print vbTab & "+ ETRS Lambert 08 -> ETRS89"
   Test ETRS89_Lambert08, ETRS89_Geo, 735000, 545000, "5°32'49.39407", "49°42'37.60005", m, dms
Debug.Print vbTab & "+ ETRS Lambert 08 -> BD72 Lambert 72"
   Test ETRS89_Lambert08, BD72_Lambert72, 735000, 545000, "234986.1", "44988.4", m, m, 1
Debug.Print vbTab & "+ BD72 Lambert 72 -> ETRS Lambert 08"
   Test BD72_Lambert72, ETRS89_Lambert08, 50000, 200000, "549996.807", "699988.150", m, m, 1
Debug.Print "---"
End Sub
Public Sub TestAlgos()
   Dim tProj As tProjection
   Dim tTrans As tTransformation
   Dim tXYZ As t_XYZ
   Dim dVal As Double
   Dim av(0 To 2) As Variant
   Dim i As Integer
   Dim sTabs As String
   sTabs = vbTab & vbTab
Debug.Print "--- Validation des algorithmes ---"
   'utilisé par alg01 et 02
   av(0) = VBA.Array(1.00552653648, 0.08199188998, 0.872664626)
   av(1) = VBA.Array(-0.3026169006, 0.08199188998, -0.29999999997)
   av(2) = VBA.Array(0.2, 0.08199188998, 0.19998903369)
   'alg01
Debug.Print vbTab & "+ alg01 - Latitude isométrique à partir de la latitude"
   For i = 0 To UBound(av)
Debug.Print sTabs & "Attendu -> phi(rad) : " & av(i)(0)
Debug.Print sTabs & "Obtenu  -> phi(rad) : " & alg01(av(i)(2), av(i)(1))
Debug.Print sTabs & "---"
   Next i
   'alg02
Debug.Print vbTab & "+ alg02 - Latitude à partir de la latitude isométrique"
   For i = 0 To UBound(av)
Debug.Print sTabs & "Attendu -> Lat iso : " & av(i)(2)
Debug.Print sTabs & "Obtenu  -> Lat iso : " & alg02(av(i)(0), av(i)(1))
Debug.Print sTabs & "---"
   Next i
   'alg03
Debug.Print vbTab & "+ alg03 - Coordonnées géo vers Lambert"
Debug.Print sTabs & "Attendu -> X : " & 1029705.0818 & " m _ Y : " & 272723.851 & " m"
   With tProj
      .tEl.de = 0.0824832568
      .dN = 0.760405966
      .dc = 11603796.9767
      .dlambda0 = 0.04079234433
      .dXs = 600000
      .dYs = 5657616.674
   End With
   tXYZ = alg03(0.145512099, 0.872664626, tProj)
Debug.Print sTabs & "Obtenu  -> X : " & Arrondi(tXYZ.dX, 4) & " m _ Y : " & Arrondi(tXYZ.dY, 4) & " m"
Debug.Print sTabs & "---"
   'alg04
Debug.Print vbTab & "+ alg04 - Lambert vers coordonnées géo "
Debug.Print sTabs & "Attendu -> lon : " & 0.14551209925 & " _ lat : " & 0.87266462567
   With tProj
      .tEl.de = 0.0824832568
      .dN = 0.760405966
      .dc = 11603796.9767
      .dlambda0 = 0.04079234433
      .dXs = 600000
      .dYs = 5657616.674
   End With
   tXYZ = alg04(1029705.083, 272723.849, tProj)
Debug.Print sTabs & "Obtenu  -> Lon : " & Arrondi(tXYZ.dX, 11) & " _ Lat : " & Arrondi(tXYZ.dY, 11)
Debug.Print sTabs & "---"
   'alg21
Debug.Print vbTab & "+ alg21 - Calcul grande Normale"
Debug.Print sTabs & "Attendu -> N : " & 6393174.9755 & " m"
   dVal = alg21(0.977384381, 6378388, 0.08199189)
Debug.Print sTabs & "Obtenu  -> N : " & Arrondi(dVal, 4) & " m"
Debug.Print sTabs & "---"
   'alg09
Debug.Print vbTab & "+ alg09 - Coordonnées géo vers cartésiennes"
Debug.Print sTabs & "Attendu -> X : " & 6376064.6955 & _
          " m _ Y : " & 111294.623 & " m _ Z : " & 128984.725 & " m"
   With tProj.tEl
      .dh = 100
      .da = 6378249.2
      .de = 0.08248325679
   End With
   tXYZ = alg09(0.01745329248, 0.02036217457, tProj.tEl)
Debug.Print sTabs & "Obtenu  -> X : " & Arrondi(tXYZ.dX, 4) & _
          " m _ Y : " & Arrondi(tXYZ.dY, 4) & " m _ Z : " & Arrondi(tXYZ.dZ, 4) & " m"
Debug.Print sTabs & "---"
Debug.Print sTabs & "Attendu -> X : " & 6378232.2149 & _
          " m _ Y : " & 18553.578 & " m _ Z : " & 0 & " m"
   With tProj.tEl
      .dh = 10
      .da = 6378249.2
      .de = 0.08248325679
   End With
   tXYZ = alg09(0.00290888212, 0, tProj.tEl)
Debug.Print sTabs & "Obtenu  -> X : " & Arrondi(tXYZ.dX, 4) & _
          " m _ Y : " & Arrondi(tXYZ.dY, 4) & " m _ Z : " & Arrondi(tXYZ.dZ, 4) & " m"
Debug.Print sTabs & "---"
   'alg12
Debug.Print vbTab & "+ alg12 - Coordonnées cartésiennes vers géo"
Debug.Print sTabs & "Attendu -> Lon : " & 0.01745329248 & _
          " _ Lat : " & 0.02036217457 & " _ h : " & 99.9995 & " m"
   With tProj.tEl
      .da = 6378249.2
      .de = 0.08248325679
   End With
   With tXYZ
      .dX = 6376064.695
      .dY = 111294.623
      .dZ = 128984.725
   End With
   tXYZ = alg12(tXYZ, tProj.tEl)
Debug.Print sTabs & "Obtenu  -> Lon : " & Arrondi(tXYZ.dX, 11) & _
          " _ Lat : " & Arrondi(tXYZ.dY, 11) & " _ h : " & Arrondi(tXYZ.dZ, 4) & " m"
Debug.Print sTabs & "---"
Debug.Print sTabs & "Attendu -> Lon : " & 0.00581776423 & _
          " _ Lat : " & -0.03199770301 & " _ h : " & 2000.0001 & " m"
   With tXYZ
      .dX = 6376897.537
      .dY = 37099.705
      .dZ = -202730.907
   End With
   tXYZ = alg12(tXYZ, tProj.tEl)
Debug.Print sTabs & "Obtenu  -> Lon : " & Arrondi(tXYZ.dX, 11) & _
          " _ Lat : " & Arrondi(tXYZ.dY, 11) & " _ h : " & Arrondi(tXYZ.dZ, 4) & " m"
Debug.Print sTabs & "---"
   'alg13
Debug.Print vbTab & "+ alg13 - Coordonnées cartésiennes vers cartésiennes"
Debug.Print sTabs & "Attendu -> Vx : " & 4154005.8099 & _
          " m _ Vy : " & -80587.3284 & " m _ Vz : " & 4823289.5316 & " m"
   With tTrans
      .dTx = -69.4
      .dTy = 18
      .dTz = 452.2
      .dRx = 0
      .dRy = 0
      .dRz = 0.00000499358
      .dFacteurEchelle = 1 - 0.00000321
   End With
   With tXYZ
      .dX = 4154088.142
      .dY = -80626.331
      .dZ = 4822852.813
   End With
   tXYZ = alg13(tXYZ, tTrans)
Debug.Print sTabs & "Obtenu  -> Vx : " & Arrondi(tXYZ.dX, 4) & _
          " m _ Vy : " & Arrondi(tXYZ.dY, 4) & " m _ Vz : " & Arrondi(tXYZ.dZ, 4) & " m"
Debug.Print sTabs & "---"
   'alg54
Debug.Print vbTab & "+ alg54 - Calcul paramètres lambert dans le cas sécant"
Debug.Print sTabs & "Attendu -> c (m): " & 11565915.8294 & _
          " m _ n : " & 0.7716421867 & " _ Xs : " & 150000 & " _ Ys : " & 5400000
   With tProj
      .tEl.da = 6378388
      .tEl.de = 0.08199189
      .dlambda0 = 0.07623554539
      .dphi0 = 1.570796327
      .dXs = 0
      .dYs = 0
      .dphi1 = 0.869755744
      .dphi2 = 0.893026801
   End With
   alg54 tProj, 150000, 5400000
Debug.Print sTabs & "Obtenu  -> c (m): " & tProj.dc & " m _ n : " & tProj.dN & _
          " _ Xs : " & tProj.dXs & " _ Ys : " & tProj.dYs
Debug.Print sTabs & "---"
Debug.Print sTabs & "Attendu -> c (m): " & -12453174.1795 & _
          " m _ n : " & -0.63049633 & " _ Xs : " & 0 & " _ Ys : " & -12453174.1795
   With tProj
      .tEl.da = 6378388
      .tEl.de = 0.08199189
      .dlambda0 = 0
      .dphi0 = 0
      .dXs = 0
      .dYs = 0
      .dphi1 = -0.575958653
      .dphi2 = -0.785398163
   End With
   alg54 tProj, 0, 0
Debug.Print sTabs & "Obtenu  -> c (m): " & tProj.dc & " m _ n : " & tProj.dN & _
          " _ Xs : " & tProj.dXs & " _ Ys : " & tProj.dYs
Debug.Print sTabs & "---"
End Sub
Private Sub Test(ByVal eDep As eSCG, ByVal eFin As eSCG, _
                 ByVal dXi As Double, dYi As Double, _
                 ByVal sXf As String, sYf As String, _
                 ByVal eUniteIn As eUnites, ByVal eUniteOut As eUnites, _
                 Optional ByVal byDecimal As Byte = 1)
   Dim tRes As tResults
   Dim sTabs As String
   sTabs = vbTab & vbTab
   Me.TypeConversion eDep, eFin
   If Me.IsErreur Then
Debug.Print sTabs & "Erreur : " & Me.Erreur
   Else
      tRes = SetUnites(dXi, dYi, eUniteIn, True)
      With tRes
         Me.CalculSCG .dX, .dY
Debug.Print sTabs & "Entrée  - Xi : " & .sX & " - Yi : " & .sY
Debug.Print sTabs & "Attendu - Xf : " & sXf & " - Yf : " & sYf
         tRes = SetUnites(Me.X, Me.Y, eUniteOut, False, byDecimal)
Debug.Print sTabs & "Obtenu  - Xc : " & .sX & " - Yc : " & .sY
      End With
   End If
End Sub
Private Function SetUnites(ByVal dX As Double, ByVal dY As Double, _
                           ByVal eUn As eUnites, ByVal bIn As Boolean, _
                           Optional ByVal byDecimal As Byte = 1) As tResults
   Dim tRes As tResults
   With tRes
      Select Case eUn
      Case eUnites.dms
         If bIn Then
            .dX = AngleDMSToRadian(dX)
            .dY = AngleDMSToRadian(dY)
            .sX = AngleDMSToString(dX, False)
            .sY = AngleDMSToString(dY, True)
         Else
            .sX = RadianToStringDMS(dX, False)
            .sY = RadianToStringDMS(dY, True)
         End If
      Case eUnites.gr
         If bIn Then
            .dX = GradeToRadian(dX)
            .dY = GradeToRadian(dY)
            .sX = dX & "gr"
            .sY = dY & "gr"
         Else
            .sX = Arrondi(RadianToGrade(dX), byDecimal) & "gr"
            .sY = Arrondi(RadianToGrade(dY), byDecimal) & "gr"
         End If
      Case eUnites.m
         If bIn Then
            .dX = dX
            .dY = dY
            .sX = dX & "m"
            .sY = dY & "m"
         Else
            .sX = Arrondi(dX, byDecimal) & "m"
            .sY = Arrondi(dY, byDecimal) & "m"
         End If
      Case eUnites.rad
         If bIn Then
            .dX = dX
            .dY = dY
            .sX = dX & "rad"
            .sY = dY & "rad"
         Else
            .sX = Arrondi(dX, byDecimal) & "rad"
            .sY = Arrondi(dY, byDecimal) & "rad"
         End If
      Case eUnites.DD
         If bIn Then
            .dX = DegreDecToRadian(dX)
            .dY = DegreDecToRadian(dY)
            .sX = dX & "°"
            .sY = dY & "°"
         Else
            .sX = Arrondi(RadianToDegreDec(dX), byDecimal) & "°"
            .sY = Arrondi(RadianToDegreDec(dY), byDecimal) & "°"
         End If
      End Select
   End With
   SetUnites = tRes
End Function
#End If