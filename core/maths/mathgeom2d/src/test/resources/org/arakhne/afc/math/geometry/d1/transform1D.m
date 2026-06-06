% Transform1D to Transform2D

output_precision(16)

A = [1,2]
B = [34, 48]

Trx = 30
Try = -6.3

V = (B-A)/norm(B-A)
P = [-(V*[0;1]), V*[1;0]]

r = V*Trx+P*Try
