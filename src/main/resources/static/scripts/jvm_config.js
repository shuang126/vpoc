export const gc_config = {
    SurvivorRatio: 8, // survivor:eden = 1:8
    NewRatio: 2 // young:old = 1:2
}
export const obj_space = {
    width: 80,
    height: 40
}
export const eden_space = {
    width: 800,
    height: 320,
}
export const survivor_space = {
    width: eden_space.width / 1,
    height: eden_space.height / 8
}
export const old_space = {
    width: eden_space.width,
    height: eden_space.height * gc_config.NewRatio
}