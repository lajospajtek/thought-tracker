#light
open System
open System.Collections.Generic
open System.Text

type get_by_name = String -> String
type accumulate_message = String -> unit
type validator = get_by_name -> accumulate_message -> bool

let _and_ (x : validator) (y : validator) = (fun (get_value_from_bag : get_by_name) (accumulate_validation_message : accumulate_message) -> 
       let ok_x = lazy x get_value_from_bag accumulate_validation_message
       let ok_y = lazy y get_value_from_bag accumulate_validation_message
       Lazy.force ok_x && Lazy.force ok_y)
        
let _or_ (x : validator) (y : validator) = (fun (get_value_from_bag : get_by_name) (accumulate_validation_message : accumulate_message) -> 
       let ok_x = lazy x get_value_from_bag accumulate_validation_message
       let ok_y = lazy y get_value_from_bag accumulate_validation_message
       Lazy.force ok_x || Lazy.force ok_y)

let _not_ (x : validator) = (fun (get_value_from_bag : get_by_name) (accumulate_validation_message : accumulate_message) -> 
       let ok_x = x get_value_from_bag accumulate_validation_message
       not ok_x)

let _xor_ (x : validator) (y : validator) = ((_not_ x) |> _and_ y ) |> _or_ (y |> _and_ (_not_ x) ) 


let is_starting_with (start : String) (name : String) = (fun (get_value_from_bag : get_by_name) (accumulate_validation_message : accumulate_message) ->
    let value = get_value_from_bag name
    let ok = value.StartsWith(start)
    if not ok then 
        let msg = string.Format("{0} '{1}' doesn't start with {2}", name, value, start)
        accumulate_validation_message msg
    ok)


let map = new Dictionary<String,String>()
map.Add("customer","ali")
map.Add("buyer", "baba")
map.Add("vendor", "nono")

let get_value (key : String) : String = map.Item(key)

let acc = new StringBuilder()
let accumulate (msg: String) : unit = acc.AppendLine(msg) |> ignore

let x = ("customer" |> is_starting_with "a") |> _and_ ("buyer" |> is_starting_with  "b") |> _and_ ("vendor" |> is_starting_with "v")

printfn "evaluation: %b messages: %s" (x get_value accumulate) (acc.ToString())

System.Console.ReadKey()
