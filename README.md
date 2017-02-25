# Mocho Project - Android

Ini adalah repository untuk Game Mocho yang digunakan pada Android.  Pada _platform_ ini, pemain dapat melakukan _training_ dan _upgrade_ suatu monster sehingga monster yang dibuat bisa menjadi lebih kuat dan dapat bertarung dengan monster lainnya.
## Fitur yang sudah dibuat
* Fitur Login dan Register suatu pemain. Apabila pemain melakukan register, maka pertamanya akan melakukan verifikasi terlebih dahulu ke e-mail yang didaftarkan baru pemain dapat memainkannya.
* Fitur tambah attack atau defense suatu monster (setiap monster memiliki penambahan yang berbeda-beda)

## Service yang dipakai
* GPS (Location Service)
* Firebase Database, Authentication and Crash Reporting
* JSON API Server (cleverapps.io)

## Cara memainkannya
1. Register terlebih dahulu. Setelah melakukan register, maka akan ada verifikasi lewat email. Verifikasi terlebih dahulu.
2. Login dengan menggunakan akun yang dipakai untuk register.
3. Pilih suatu monster yang akan diupgrade.
4. Pergi ke suatu tempat yang dekat dengan marker yang ada di maps yang tersedia. Jika jarak antara lokasi tersebut dengan lokasi anda kurang dari suatu nilai, maka anda dapat meng-klik lokasi tersebut.
5. Setelah lokasi tersebut ditekan, maka anda dapat melakukan upgrade monster tersebut.

## Kekurangan
1. Tidak dapat melakukan _summon_ monster (masih harus memasukkan monster secara manual) karena _summon_ monster terdapat pada platform _arduino_
2. Maps masih terus menerus melakukan zoom in ke tempat berada, sehingga User Experience menjadi sedikit terganggu
## Pembagian Tugas
13514068 : Membagi tugas, notification, API Server initialization, Firebase, Login & Register
13514086 : Desain Game Monster, Attack, Defense
13514088 : Location Service (GPS) in Real Time, Marker lokasi, Laporan dan README