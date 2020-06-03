let gc_config = {
    SurvivorRatio: 8, // survivor:eden = 1:8
    NewRatio: 2 // young:old = 1:2
}
let colors = ["yellow", "red", "pink", "green", "blue"]

let obj_state = {
    width: 80,
    height: 40,
    get area() {
        return this.width * this.height;
    }
}

let new_dot = Snap('#heap').circle(500, 50, 10).attr(
    {
        stroke: "blue",
        "stroke-width": 3,
        fill: "none"
    }
);

let eden_space = {
    current_x: 0,
    current_y: 0,
    allocated: 0,
    width: 800,
    height: 320,
    to_s: 0,
    lives: [], // live object ids after marking
    start_obj_id: 0, // 0 means empty
    next_obj_id: 1, // obj id starts from 1
    get area() {
        return this.width * this.height;
    },
    get eden_capacity() {
        return this.area / obj_state.area
    },
    get find_live_obj() {
        return Math.floor(Math.random() * (this.next_obj_id - this.start_obj_id)) + this.start_obj_id;
    },
    switch_survivor: function () {
        this.to_s = 1 - this.to_s;
    },
    move_start_obj_id: function () {
        this.start_obj_id = this.next_obj_id;
    },
    mark: function (live_obj_id) {
        let obj = Snap(`#obj-${live_obj_id}`);
        obj.select('rect').animate({fill: 'white'}, 3000, mina.bounce);
        Snap.animate(0, 1, function (value) {
            obj.select('text').attr({'font-size': 100, x: 15, y: 35, opacity: value});
        }, 500, mina.bounce);
    },
    copy: function () {
        let s = Snap(`#s${this.to_s}`);
        let offset_x = 0;
        let offset_y = 2;
        for (let i = 0; i < 3; i++) {
            let obj = Snap(`#obj-${this.lives[i]}`).clone().attr({id: `copy-obj-${this.lives[i]}`});
            s.add(obj);
            obj.animate({transform: `translate(${offset_x} ${offset_y})`}, 1000, mina.linear);
            offset_x += obj.select('rect').getBBox().width;
            console.log(`offset: ${offset_x}`)
        }
    },
    sweep: function () {

    },
    move_pointer: function (width) {
        this.current_x += width;
        if (this.current_x == this.width) {// next row
            this.current_x = 0;
            this.current_y += obj_state.height;
        }
    },
    draw: function (cut_width) {
        setTimeout(function () {
            new_dot.attr({cx: 500, cy: 80});
            new_dot.animate({cx: 500, cy: 150}, 100, mina.linear);
        }, 10);
        let eden = Snap('#eden');
        let obj_g = eden.g().attr({
            id: `obj-${this.next_obj_id}`,
            transform: `translate(${this.current_x}, ${this.current_y})`
        });
        obj_g.rect(0, 0, cut_width, obj_state.height)
            .attr({
                fill: colors[Math.floor(Math.random() * 5)],
                stroke: "gray",
                "stroke-width": 2,
            })
            .addClass("obj_new");
        obj_g.text(cut_width / 2, obj_state.height / 2 + 5, this.next_obj_id);
    },
    allocate_one_obj: function (obj_size) {
        console.log(`eden_space before allocate_one_obj: ${JSON.stringify(eden_space)}`);
        let cut_width = obj_state.width * obj_size;
        if (this.current_x + cut_width > this.width) { // width overflow
            cut_width = this.width - this.current_x; // cut
        }
        let obj_id = this.next_obj_id;
        this.draw(cut_width);
        this.move_pointer(cut_width);
        this.allocated += cut_width / obj_state.width;

        this.next_obj_id++;

        console.log(`eden_space after allocate_one_obj: ${JSON.stringify(eden_space)}`);
        return obj_id;
    },
    minor_gc: function () {
        console.log(`eden_space before gc: ${JSON.stringify(eden_space)}`);
        this.mark();
        this.copy();
        this.switch_survivor();
        this.move_start_obj_id();
        this.allocated = 0;
        console.log(`eden_space after gc: ${JSON.stringify(eden_space)}`);
    }
}

let survivor0 = {
    width: eden_space.width / 1,
    height: eden_space.height / 8
}

let survivor1 = {
    width: eden_space.width / 1,
    height: eden_space.height / 8
}

let old = {
    width: eden_space.width,
    height: eden_space.height * gc_config.NewRatio
}

function init_heap() {
    let young = Snap('#young');
    let eden = young.g().attr({id: 'eden', transform: "translate(0 0)"});
    eden.rect(0, 0, eden_space.width, eden_space.height).addClass('eden');

    let s0_g = young.g().attr({id: 's0', transform: `translate(0 ${eden_space.height + 20})`});
    s0_g.rect(0, 0, survivor0.width, survivor0.height).addClass('survivor');
    let s1_g = young.g().attr({id: 's1', transform: `translate(0 ${eden_space.height + 80})`});
    s1_g.rect(0, 0, survivor1.width, survivor1.height).addClass('survivor');

    let old_g = Snap('#old');
    old_g.rect(0, 0, old.width, old.height).addClass('old');
}

function new_objects() {
    let obj_count = 58;
    while (obj_count > 0) {
        let obj_size = Math.ceil(Math.random() * 2);
        setTimeout(function () {
            eden_space.allocate_one_obj(obj_size);
        }, 100 * obj_count);

        obj_count--;
    }
}
