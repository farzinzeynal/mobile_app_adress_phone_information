package com.example.user.specialinformation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class UserModel
{
    private  int Id;
    private  String name;
    private  String phoneNumber;
    private  String adress;
    byte [] image;
}
