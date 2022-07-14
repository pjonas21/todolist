import 'package:flutter/material.dart';
import 'package:todolist/todo_list_page.dart';

void main(List<String> args) {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Todo - List',
      theme: ThemeData(
        primaryColor: Colors.green,
      ),
      home: TodoListPage(),
    );
  }
}