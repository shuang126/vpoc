import {eden_space, survivor_space, old_space, obj_space} from "./jvm_config.js";

let colors = [
    "#a8e6cf", "#dcedc1", "#ffd3b6", "#ffaaa5",
    "#ff3377", "#ee4035", "#f37736", "#fdf498",
    "#7bc043", "#0392cf", "#8595a1", "#58668b",
]

let new_dot = Snap('#heap').circle(600, 80, 10).attr(
    {
        stroke: "blue",
        "stroke-width": 3,
        fill: "none"
    }
);

export const minor_gc = {
    new: function (obj) {
        setTimeout(function () {
            new_dot.attr({cx: 600, cy: 80});
            new_dot.animate({cx: 600, cy: 130}, 100, mina.linear);
        }, 10);

        let obj_pointer = obj.address * obj_space.width;
        let x_offset = obj_pointer % eden_space.width;
        let y_offset = Math.floor(obj_pointer / eden_space.width) * obj_space.height;
        let obj_id = obj.id;
        let obj_width = obj.size * obj_space.width;

        let eden = Snap('#eden');
        let obj_g = eden.g().attr({
            id: `obj-${obj_id}`,
            transform: `translate(${x_offset}, ${y_offset})`
        });
        obj_g.rect(0, 0, obj_width, obj_space.height)
            .attr({
                fill: colors[Math.floor(Math.random() * 12)],
                stroke: "gray",
                "stroke-width": 1,
            })
            .addClass("obj_new");
        obj_g.text(obj_width / 2, obj_space.height / 2 + 5, obj_id)
            .attr({'text-anchor': 'middle', "font-size": 20});
    },

    mark: function (obj) {
        let obj_id = obj.id;
        let obj_g = Snap(`#obj-${obj_id}`);
        obj_g.select('rect').animate({fill: 'white'},
            3000, mina.bounce,
            () => {
                obj_g.select('rect').animate(
                    {width: obj_space.width, height: obj_space.height},
                    3000, mina.bounce);
            });
        Snap.animate(0, 1, function (value) {
            obj_g.select('text').attr({'font-size': 50, opacity: value});
        }, 500, mina.bounce);
    },

    copy: function (c) {
        let from = c.from.toLowerCase();
        let to = c.to.toLowerCase();
        let obj_id = c.objectBO.id;
        let address = c.address;
        let offset_x = address * obj_space.width;
        let offset_y = 1;
        let obj_g = Snap(`#obj-${obj_id}`).clone().attr({id: `obj-${obj_id}`});
        Snap(`#${to}`).add(obj_g);
        obj_g.animate(
            {
                transform: `translate(${offset_x} ${offset_y})`
            },
            1000, mina.linear);
        obj_g.select('text').attr({"font-size": 20});
    },

    sweep: function (s) {
        let space = s.space.toLowerCase();
        Snap(`#${space}`).selectAll('g').remove();
    }
}

export function init_heap() {
    let young_g = Snap('#young');
    young_g.rect(-10, -10, eden_space.width + 20, eden_space.height + survivor_space.height * 2 + 60)
        .addClass('young');
    young_g.text(-120, 10, "年轻代").addClass('title');
    young_g.text(-120, 50, "Young").addClass('title');

    let eden_g = young_g.g().attr({id: 'eden', transform: "translate(0 0)"});
    eden_g.rect(0, 0, eden_space.width, eden_space.height).addClass('eden');
    eden_g.text(-120, 160, "伊甸区").addClass('sub_title');
    eden_g.text(-120, 200, "Eden").addClass('sub_title');

    let s0_g = young_g.g().attr({id: 's0', transform: `translate(0 ${eden_space.height + 20})`});
    s0_g.rect(0, 0, survivor_space.width, survivor_space.height).addClass('survivor');
    s0_g.text(-140, 30, "存活区0").addClass('sub_title');
    s0_g.text(-180, 30, "S0").addClass('sub_title');

    let s1_g = young_g.g().attr({id: 's1', transform: `translate(0 ${eden_space.height + 80})`});
    s1_g.rect(0, 0, survivor_space.width, survivor_space.height).addClass('survivor');
    s1_g.text(-140, 30, "存活区1").addClass('sub_title');
    s1_g.text(-180, 30, "S1").addClass('sub_title');

    let old_g = Snap('#old');
    old_g.rect(0, 0, old_space.width, old_space.height).addClass('old');
    old_g.text(-120, 100, "老年代").addClass('title');
    old_g.text(-120, 160, "Old").addClass('title');
}